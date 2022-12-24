package me.agronaut.springrest.repository;

import me.agronaut.springrest.model.Finance;
import me.agronaut.springrest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRepository extends PagingAndSortingRepository<Finance, Long> {

    List<Finance> getAllByUser(User user);
}
