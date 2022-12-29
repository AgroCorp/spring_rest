package me.agronaut.springrest.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * A DTO for the {@link User} entity
 */
@Data
@Getter
@Setter
public class UserDto implements Serializable {
    private final Long id;
    private final String firstName;
    private final String lastName;
    @NotNull
    private final String username;
    @NotNull
    private final String password;
    @NotNull
    @Email
    private final String email;
    @NotNull
    private final Date registrationDate;
    @NotNull
    private final Boolean active;
    private final List<RoleDto> roles;
}
