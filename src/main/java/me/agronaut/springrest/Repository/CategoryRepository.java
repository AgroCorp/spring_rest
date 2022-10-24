package me.agronaut.springrest.Repository;

import me.agronaut.springrest.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> getCategoryById(Long id);
}
