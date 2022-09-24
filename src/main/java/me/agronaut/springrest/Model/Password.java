package me.agronaut.springrest.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Table(name = "PASSWORD")
public class Password {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PASSWORD_ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "CRD") @CreatedDate
    private LocalDateTime crd;

    @Column(name = "CRU") @CreatedBy
    private String cru;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "USER_ID")
    private User user;
}