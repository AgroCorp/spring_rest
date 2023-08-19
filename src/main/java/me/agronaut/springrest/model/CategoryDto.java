package me.agronaut.springrest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A DTO for the {@link Category} entity
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {
    private Long id;
    @NotNull(message = "Name field is required!")
    private String name;

    public String toJson() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (id != null) {
            sb.append("\"id\":%d,".formatted(id));
        }
        if (name != null) {
           sb.append("\"name\":\"%s\"".formatted(name));
        }

        sb.append("}");

        return sb.toString();
    }
}
