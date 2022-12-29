package me.agronaut.springrest.repository;

import me.agronaut.springrest.model.Finance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceRepository extends JpaRepository<Finance, Long> {
}
