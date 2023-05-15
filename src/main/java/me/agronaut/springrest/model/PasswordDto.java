package me.agronaut.springrest.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link Password} entity
 */
@Data
@Getter
@Setter
@NoArgsConstructor
public class PasswordDto implements Serializable {
    private  Long id;
    private  String name;
    private  String value;
    private  String webPage;
    private  LocalDateTime crd;
    private  String cru;
    private  LocalDateTime lmd;
    private  UserDto user;
}
