package me.agronaut.springrest.Model;

import lombok.*;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDao implements Serializable {
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Set<String> roles;
    private String token;
    private Boolean active;

    public UserDao(User user) {
        this.token = user.getToken();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        this.active = user.getActive();
    }
}
