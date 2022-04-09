package me.agronaut.springrest.Repository;

import me.agronaut.springrest.Model.SessionUsers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionUserRepository extends JpaRepository<SessionUsers, Long> {
    public SessionUsers getByToken(String token);
}
