package me.agronaut.springrest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import me.agronaut.springrest.model.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    Optional<User> getUserByUsername(String username);

    User getUserByEmail(String email);

    User getUserById(Long id);

    boolean existsUserByEmail(String email);

    @Query("select distinct u from User u inner join Role r on r.user = u where r.name = :role")
    List<User> findAllUserByRoleName(String role);
}
