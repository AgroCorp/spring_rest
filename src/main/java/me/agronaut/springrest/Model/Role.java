package me.agronaut.springrest.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ROLES")
@Getter
@Setter
@Component
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROLE_ID")
    @NotNull
    private Long id;

    @Column(name = "NAME")
    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private User user;
}
