package me.agronaut.springrest.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    @NotNull
    private String name;
    @Column(name = "AMOUNT", nullable = false)
    private Long amount;
    @ManyToOne(targetEntity = Category.class, optional = false)
    @JoinColumn(name = "CATEGORY_ID")
    @NotNull
    private Category category;
    @ManyToOne(targetEntity = User.class, optional = false)
    @JoinColumn(name = "USER_ID")
    @NotNull
    private User user;
    @Column(name = "IS_INCOME")
    @NotNull
    private Boolean income;
}
