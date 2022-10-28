package me.agronaut.springrest.Repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import me.agronaut.springrest.Model.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> getUserByUsername(String username);

    User getUserByEmail(String email);

    User getUserById(Long id);

    Boolean existsUserByEmail(String email);
}
