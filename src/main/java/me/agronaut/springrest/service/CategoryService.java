package me.agronaut.springrest.service;

import me.agronaut.springrest.model.Category;
import me.agronaut.springrest.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    public Category getByIndex(Long id) {
        return categoryRepository.getCategoryById(id).orElseThrow();
    }
}
