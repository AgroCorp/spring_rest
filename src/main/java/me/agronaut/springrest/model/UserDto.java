package me.agronaut.springrest.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class UserDto implements Serializable {
    private  Long id;
    private  String firstName;
    private  String lastName;
    @NotNull
    private  String username;
    @NotNull
    private  String password;
    @NotNull
    @Email
    private  String email;
    @NotNull
    private  Date registrationDate;
    @NotNull
    private  Boolean active;
    @JsonManagedReference
    private  List<RoleDto> roles;
}
