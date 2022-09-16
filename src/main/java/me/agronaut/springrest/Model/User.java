package me.agronaut.springrest.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@Component
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "first_name") private String firstName;
    @Column(name = "last_name") private String lastName;
    @Column(name = "username", unique = true)
    @NotNull private String username;
    @Column(name = "password") @NotNull private String password;
    @Column(name = "email", unique = true) @NotNull @Email private String email;
    @Column(name = "registration_date") @NotNull @CreatedDate private Date registrationDate;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Role> roles;

    @Transient
    private String token;
}
