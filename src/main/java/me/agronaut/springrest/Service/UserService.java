package me.agronaut.springrest.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    UserRepository userRepo;
    @Autowired
    PasswordEncoder encoder;
    @PersistenceContext
    private EntityManager entityManager;

    public User save(User newUser)
    {
        newUser.setPassword(encoder.encode(newUser.getPassword()));
        newUser.setRegistrationDate(new Date());
        return userRepo.save(newUser);
    }

    public String login(User loginUser)
    {
        User login = userRepo.getUserByUsername(loginUser.getUsername());
        if (login != null)
        {
            if (encoder.matches(loginUser.getPassword(), login.getPassword()))
            {
                // insert session table
                return getJWTToken(login.getUsername());
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

    private String getJWTToken(String username){
        String secretKey = "v4j4s.k3ny3r?HaGyMa$VaL!";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("RestAgroTest")
                .setSubject(username)
                .claim("authorities", grantedAuthorities.stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 600000))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes()).compact();

        return "Bearer " + token;
    }
}
