package me.agronaut.springrest.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

    @Column(name = "VALUE", nullable = false)
    private String value;

    @Column(name = "CRD") @CreatedDate
    private LocalDateTime crd;

    @Column(name = "CRU") @CreatedBy
    private String cru;

    @Column(name = "LMD") @LastModifiedDate
    private LocalDateTime lmd;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}
