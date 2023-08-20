package me.agronaut.springrest.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.agronaut.springrest.model.Role;
import me.agronaut.springrest.model.User;
import me.agronaut.springrest.model.UserDto;
import me.agronaut.springrest.repository.UserRepository;
import me.agronaut.springrest.util.LogUtil;
import me.agronaut.springrest.util.Statics;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserRepository userRepo;
    private final EmailService emailService;
    private final LogUtil logger = new LogUtil(UserService.class);

    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepo, EmailService emailService, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @PersistenceContext
    private EntityManager entityManager;

    @Value("${apiUrl}")
    private String apiUrl;
    @Value("${jwt.secret}")
    private String secretKey;

    public User register(User newUser) throws UserExistByEmailException {
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        newUser.setRegistrationDate(new Date());
        newUser.setActive(false);
        logger.debug("new user save to the database", "newUser", newUser);

        if (userRepo.existsUserByEmail(newUser.getEmail())) {
            logger.debug("Email cim mar foglalat!!");
            throw new UserExistByEmailException("User exists with given email address!");
        }

        User registeredUser = userRepo.save(newUser);

        String token = Base64Utils.encodeToUrlSafeString(newUser.getId().toString().getBytes());
        logger.debug("token:", "token", token);
        emailService.sendEmail(null, registeredUser.getEmail(), "Confirm Registration",
                "Hello " + registeredUser.getUsername() + "!<br /><br />" +
                        "Please click the link below to <a href=" +
                        apiUrl + "/activate/" + token + ">activate your account</a>.");

        return registeredUser;
    }

    public void activate(Long userId) {
        logger.debug("activate methos - START");
        logger.debug("user id in activate method:", "userId", userId);
        User user = userRepo.getUserById(userId);

        if (user != null) {
            user.setActive(true);

            userRepo.save(user);
        } else {
            throw new EntityNotFoundException("User with given ID does not exist");
        }
    }

    public User login(User loginUser) throws NotActiveUserException, EntityNotFoundException {
        if (loginUser == null) {
            throw new EntityNotFoundException("login user is null");
        }

        Optional<User> login = userRepo.getUserByUsername(loginUser.getUsername());
        if (login.isPresent()) {
            logger.debug("getted user", "User", login.get());
            User casted = login.get();
            if (casted.getActive() != null && !casted.getActive()) {
                throw new NotActiveUserException("user is not activated");
            }
            if (encoder.matches(loginUser.getPassword(), casted.getPassword())) {
                logger.debug("username and password match!");
                casted.setToken(getJWTToken(casted.getUsername(), casted.getRoles()));
                return casted;
            } else {
                throw new EntityNotFoundException("Password is incorrect");
            }
        } else {
            throw new EntityNotFoundException("Cannot find user with given username");
        }
    }

    public User getById(Long id) {
        return userRepo.getUserById(id);
    }

    public Page<User> getAll(User user, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> query = builder.createQuery(User.class);

        Root<User> root = query.from(User.class);

        LinkedList<Predicate> whereCauses = new LinkedList<>();
        if (user != null) {
            if (user.getFirstName() != null && !user.getFirstName().isBlank()) {
                whereCauses.add(builder.like(root.get("firstName"), "%" + user.getFirstName() + "%"));
            }
            if (user.getLastName() != null && !user.getLastName().isBlank()) {
                whereCauses.add(builder.like(root.get("lastName"), "%" + user.getLastName() + "%"));
            }
            if (user.getUsername() != null && !user.getUsername().isBlank()) {
                whereCauses.add(builder.equal(root.get("username"), user.getUsername()));
            }
            if (user.getEmail() != null && !user.getEmail().isBlank()) {
                whereCauses.add(builder.equal(root.get("email"), user.getEmail()));
            }
            if (user.getActive() != null) {
                whereCauses.add(builder.equal(root.get("active"), user.getActive()));
            }
        }

        query.select(root).where(builder.and(whereCauses.toArray(new Predicate[0])));

        int allCount = entityManager.createQuery(query).getResultList().size();
        List<User> res = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        return new PageImpl<>(res, pageable, allCount);
    }

    private String getJWTToken(String username, List<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = roles != null ?
                AuthorityUtils.commaSeparatedStringToAuthorityList(roles.stream().map(Role::getName).collect(Collectors.joining(", "))) :
                AuthorityUtils.commaSeparatedStringToAuthorityList("");

        logger.debug("belepett user jogai:", "ROLES", roles == null ? null : roles.toString());

        return Jwts
                .builder()
                .setId("RestAgroTest")
                .setSubject(username)
                .claim("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority).toList())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
    }

    public void resetPassword(String email) {
        User user = userRepo.getUserByEmail(email);

        if (user != null) {
            String token = Base64Utils.encodeToUrlSafeString(user.getId().toString().getBytes());
            logger.debug("token after encode:", "token", token);
            emailService.sendEmail(null, user.getEmail(), "Password Reset",
                    "Hello " + user.getUsername() + "<br /><br />"
                            + "This is a password resetting e-mail.<br />"
                            + "Please click to link below to reset your password!<br />"
                            + "<a href=" + apiUrl + "/set_new_password/" + token + ">Click here!</a>");
        } else {
            throw new EntityNotFoundException("This email not associated for any user");
        }
    }

    public User setNewPassword(String newPassword, Long userId) {
        if(StringUtils.isEmpty(newPassword) || userId == null) {
            throw new NoSuchElementException(Statics.NULL_OBJECT);
        }

        User user = userRepo.getUserById(userId);

        if (user == null) {
            throw new NoSuchElementException(Statics.NULL_OBJECT);
        }

        user.setPassword(encoder.encode(newPassword));

        userRepo.save(user);

        return user;
    }

    public User getByUsername(String name) {
        if (name != null && !name.isEmpty()) {
            return userRepo.getUserByUsername(name).orElseThrow(() -> new EntityNotFoundException("User not found with given username"));
        } else {
            throw new EntityNotFoundException("Name is mandatory");
        }
    }

    /**
     * @param roleName role's name example: ADMIN or USER
     * @return pageable object with users which have role with given name
     */
    public List<UserDto> getAllByRole(String roleName) {
        List<User> all = userRepo.findAllUserByRoleName(roleName);

        return all.stream().map(iter -> modelMapper.map(iter, UserDto.class)).toList();
    }

    public static class NotActiveUserException extends Exception {
        public NotActiveUserException(String cause) {
            super(cause);
        }
    }

    public static class UserExistByEmailException extends Exception {
        public UserExistByEmailException(String cause) {
            super(cause);
        }
    }
}
