package me.agronaut.springrest.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link Category} entity
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class CategoryDto implements Serializable {
    private Long id;
    @NotNull(message = "Name field is required!")
    private String name;
}
