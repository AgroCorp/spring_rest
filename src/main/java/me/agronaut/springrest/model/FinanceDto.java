package me.agronaut.springrest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
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

    private LocalDateTime crd;
}
