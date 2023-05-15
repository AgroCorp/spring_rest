package me.agronaut.springrest.repository;

import me.agronaut.springrest.model.Finance;
import me.agronaut.springrest.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinanceRepository extends PagingAndSortingRepository<Finance, Long> {
    Optional<Finance> getById(Long id);
    List<Finance> getAllByUser(User user);
}
