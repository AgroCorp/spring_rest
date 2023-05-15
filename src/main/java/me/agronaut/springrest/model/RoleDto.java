package me.agronaut.springrest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link Role} entity
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class RoleDto implements Serializable {
    private  Long id;
    private  String name;
    @JsonBackReference
    private  UserDto user;
}
