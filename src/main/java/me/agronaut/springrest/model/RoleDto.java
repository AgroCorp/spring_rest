package me.agronaut.springrest.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * A DTO for the {@link Role} entity
 */
@Data
@Getter
@Setter
public class RoleDto implements Serializable {
    private final Long id;
    private final String name;
    private final UserDto user;
}
