package me.agronaut.springrest.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import me.agronaut.springrest.Model.Role;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserService {
    @Autowired
    UserRepository userRepo;

    @Autowired
    private EmailService emailService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    @PersistenceContext
    private EntityManager entityManager;

    public User register(User newUser)
    {
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        newUser.setRegistrationDate(new Date());
        newUser.setActive(false);
        log.debug("new user", newUser);
        User registeredUser = userRepo.save(newUser);

        String token = Arrays.toString(Base64Utils.encode(registeredUser.getId().toString().getBytes()));

        emailService.sendEmail(EmailService.NO_REPLY_ADDRESS, registeredUser.getEmail(), "Confirm Registration",
                "Hello" + registeredUser.getUsername() + "!\n\n" +
                "Please click the link below to activate your account" +
                "http://www.sativus.space/auth/activate/" + token);

        return registeredUser;
    }

    public User login(User loginUser)
    {
        if (loginUser == null) {
            throw new EntityNotFoundException("login user is null");
        }

        User login = userRepo.getUserByUsername(loginUser.getUsername());
        if (login != null)
        {
            log.info("user not null");
            if (encoder.matches(loginUser.getPassword(), login.getPassword()))
            {
                log.info("username and password match!");
                login.setToken(getJWTToken(login.getUsername(), login.getRoles()));
                return login;
            }
            else {
                throw new EntityNotFoundException("Password is incorrect");
            }
        } else {
            throw new EntityNotFoundException("Cannot find user with given username");
        }
    }

    public List<User> getAll(MultiValueMap<String, String> formData)
    {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();

        CriteriaQuery<User> query = builder.createQuery(User.class);

        Root<User> root = query.from(User.class);

        if (formData != null && !formData.isEmpty()) {
            for (Map.Entry<String, List<String>> iter : formData.entrySet()) {
                log.info("key: " + iter.getKey() + "\tvalue: " + iter.getValue());
                if(!iter.getKey().equals("{}") && !iter.getValue().get(0).equals("[]")) {
                    query.where(builder.and(builder.equal(root.get(iter.getKey()), iter.getValue().get(0))));
                }
            }
        }

        TypedQuery<User> typedQuery = entityManager.createQuery(query);

        return  typedQuery.getResultList();
    }

    private String getJWTToken(String username, List<Role> roles){
        String secretKey = "v4j4s.k3ny3r?HaGyMa$VaL!";
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

        if(user != null) {
            String token = Base64Utils.encodeToUrlSafeString(user.getId().toString().getBytes());

            emailService.sendEmail(EmailService.NO_REPLY_ADDRESS, user.getEmail(), "Password Reset",
            "Hello " + user.getUsername() + "\n\n"
                + "This is a password resetting e-mail.\n"
                + "Please click to link below to reset your password!\n"
                + "https://www.sativus.space/set_new_password/" + token);
        } else {
            throw new EntityNotFoundException("This email not associated for any user");
        }
    }

    public User set_new_password(String newPassword, String token) {
        Long userId = Long.parseLong(Arrays.toString(Base64Utils.decodeFromUrlSafeString(token)));

        User user = userRepo.getById(userId);

        user.setPassword(encoder.encode(newPassword));

        userRepo.save(user);

        return user;
    }
}
