package me.agronaut.springrest.Repository;

import me.agronaut.springrest.Model.Password;
import me.agronaut.springrest.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepository extends JpaRepository<Password, Long> {
    List<Password> findAllByUser(User user);
}
