package me.agronaut.springrest.Repository;

import me.agronaut.springrest.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByUsername(String username);
    User getUserByEmail(String email);

    User getUserById(Long id);

    Boolean existsUserByEmail(String email);
}
