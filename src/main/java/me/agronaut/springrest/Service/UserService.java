package me.agronaut.springrest.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.Role;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@Log4j2
public class UserService {
    UserRepository userRepo;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepo, EmailService emailService) {
        this.userRepo = userRepo;
        this.emailService = emailService;
    }

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
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
        log.debug("new user save to the database: \n\t{}", newUser);

        if (userRepo.existsUserByEmail(newUser.getEmail())) {
            log.warn("Email cim mar foglalat!!");
            throw new UserExistByEmailException("User exists with given email address!");
        }

        User registeredUser = userRepo.save(newUser);

        String token = Base64Utils.encodeToUrlSafeString(newUser.getId().toString().getBytes());
        log.debug("\n\ttoken: {}", token);
        emailService.sendEmail(null, registeredUser.getEmail(), "Confirm Registration",
                "Hello " + registeredUser.getUsername() + "!<br /><br />" +
                "Please click the link below to <a href=" +
                apiUrl + "/activate/" + token + ">activate your account</a>.");

        return registeredUser;
    }

    public void activate(Long userId) {
        log.info("activate methos - START");
        log.debug("{}\n\t[{}]\t{}", "user id in activate method:", "userId" ,userId);
        User user = userRepo.getUserById(userId);

        user.setActive(true);

        userRepo.save(user);
    }

    public User login(User loginUser) throws NotActiveUserException {
        if (loginUser == null) {
            throw new EntityNotFoundException("login user is null");
        }

        User login = userRepo.getUserByUsername(loginUser.getUsername());
        if (login != null) {
            if (!login.getActive()) {
                throw new NotActiveUserException("user is not activated");
            }
            if (encoder.matches(loginUser.getPassword(), login.getPassword())) {
                log.debug("username and password match!");
                login.setToken(getJWTToken(login.getUsername(), login.getRoles()));
                return login;
            } else {
                throw new EntityNotFoundException("Password is incorrect");
            }
        } else {
            throw new EntityNotFoundException("Cannot find user with given username");
        }
    }

    public List<User> getAll(User user) {
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

        return entityManager.createQuery(query).getResultList();
    }

    private String getJWTToken(String username, List<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = roles != null ?
                AuthorityUtils.commaSeparatedStringToAuthorityList(roles.stream().map(Role::getName).collect(Collectors.joining(", "))) :
                AuthorityUtils.commaSeparatedStringToAuthorityList("");


        return Jwts
                .builder()
                .setId("RestAgroTest")
                .setSubject(username)
                .claim("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();
    }

    public void reset_password(String email) {
        User user = userRepo.getUserByEmail(email);

        if (user != null) {
            String token = Base64Utils.encodeToUrlSafeString(user.getId().toString().getBytes());
            log.debug("{}\n\t[{}]\t{}","token after encode:", "token", token);
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
        return userRepo.getUserByUsername(name);
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
