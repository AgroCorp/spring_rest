package me.agronaut.springrest.Model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "session_users")
public class SessionUsers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "session_id", nullable = false)
    @NotNull
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "access_token", nullable = false)
    private String token;
}
