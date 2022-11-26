package me.agronaut.springrest.repository;

import me.agronaut.springrest.model.Password;
import me.agronaut.springrest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {
    List<Password> findAllByUser(User user);
}
