package me.agronaut.springrest.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.agronaut.springrest.Model.Role;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Repository.UserRepository;
import me.agronaut.springrest.Util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    UserRepository userRepo;
    private final EmailService emailService;
    private final LogUtil logger = new LogUtil(UserService.class);

    @Autowired
    public UserService(UserRepository userRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.emailService = emailService;
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
        logger.debug("user id in activate method:", "userId" ,userId);
        User user = userRepo.getUserById(userId);

        user.setActive(true);

        userRepo.save(user);
    }

    public User login(User loginUser) throws NotActiveUserException, EntityNotFoundException {
        if (loginUser == null) {
            throw new EntityNotFoundException("login user is null");
        }

        Optional<User> login = userRepo.getUserByUsername(loginUser.getUsername());
        logger.debug("getted user", "User", login.get());
        if (login.isPresent()) {
            User casted = login.get();
            if (!casted.getActive()) {
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

        int allCount =  entityManager.createQuery(query).getResultList().size();
        List<User> res =  entityManager.createQuery(query).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

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

    public void reset_password(String email) {
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

    public User set_new_password(String newPassword, Long userId) {
        User user = userRepo.getUserById(userId);

        user.setPassword(encoder.encode(newPassword));

        userRepo.save(user);

        return user;
    }

    public User getByUsername(String name) {
        return userRepo.getUserByUsername(name).orElse(null);
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
