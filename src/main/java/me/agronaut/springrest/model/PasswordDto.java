package me.agronaut.springrest.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link Password} entity
 */
@Data
@Getter
@Setter
public class PasswordDto implements Serializable {
    private final Long id;
    private final String name;
    private final String value;
    private final LocalDateTime crd;
    private final String cru;
    private final LocalDateTime lmd;
    private final UserDto user;
}
