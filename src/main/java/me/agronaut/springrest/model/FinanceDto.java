package me.agronaut.springrest.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link Finance} entity
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class FinanceDto implements Serializable {
    private Long id;
    @NotNull(message = "Name field is required!")
    private String name;
    @NotNull(message = "Amount field is required!")
    private Long amount;
    @NotNull(message = "Category field is required!")
    private CategoryDto category;
    private UserDto user;
    @NotNull(message = "Income field is required!")
    private Boolean income;
    @NotNull(message = "Repeatable is required!")
    private Boolean repeatable;
    private LocalDateTime repeatDate;

    private LocalDateTime crd;
}
