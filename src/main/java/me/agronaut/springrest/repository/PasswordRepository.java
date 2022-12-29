package me.agronaut.springrest.repository;

import me.agronaut.springrest.model.Password;
import me.agronaut.springrest.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface PasswordRepository extends PagingAndSortingRepository<Password, Long> {
    List<Password> findAllByUser(User user);

    Password getById(Long id);
}
