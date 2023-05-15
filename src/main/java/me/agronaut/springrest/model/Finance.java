package me.agronaut.springrest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name="FINANCE")
public class Finance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FINANCE_ID")
    private Long id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "AMOUNT", nullable = false)
    private Long amount;
    @ManyToOne(targetEntity = Category.class, optional = false)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;
    @ManyToOne(targetEntity = User.class, optional = false)
    @JoinColumn(name = "USER_ID")
    private User user;
    @Column(name = "IS_INCOME", nullable = false)
    private Boolean income;

    @CreatedDate
    private LocalDateTime crd;
}
