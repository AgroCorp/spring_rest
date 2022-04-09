package me.agronaut.springrest.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import me.agronaut.springrest.Model.SessionUsers;
import me.agronaut.springrest.Model.User;
import me.agronaut.springrest.Repository.SessionUserRepository;
import me.agronaut.springrest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    SessionUserRepository sessionRepo;

    @Autowired
    PasswordEncoder encoder;

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
                String token = getJWTToken(login.getUsername());
                // insert session table
                SessionUsers session = new SessionUsers();
                session.setUser(login);
                session.setToken(token);

                sessionRepo.save(session);

                return token;
            }
            else {
                throw new EntityNotFoundException("Password is incorrect");
            }
        } else {
            throw new EntityNotFoundException("Cannot find user with given username");
        }
    }

    public List<User> getall()
    {
        return userRepo.findAll();
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

    public Boolean logout(String token)
    {
        SessionUsers session = sessionRepo.getByToken(token);
        if(session != null) {
            sessionRepo.delete(session);
            return true;
        } else {
            return false;
        }
    }
}
