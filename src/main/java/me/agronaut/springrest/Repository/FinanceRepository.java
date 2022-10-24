package me.agronaut.springrest.Repository;

import me.agronaut.springrest.Model.Finance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinanceRepository extends JpaRepository<Finance, Long> {
}
