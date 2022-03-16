package me.agronaut.springrest.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "first_name") private String firstName;
    @Column(name = "last_name") private String lastName;
    @Column(name = "username", unique = true)
    @NotNull private String username;
    @Column(name = "password") @NotNull private String password;
    @Column(name = "email", unique = true) @NotNull @Email private String email;
    @Column(name = "registration_date") @NotNull @CreatedDate private Date registrationDate;
}