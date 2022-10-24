package me.agronaut.springrest.Service;

import me.agronaut.springrest.Model.Category;
import me.agronaut.springrest.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository repository) {
        this.categoryRepository = repository;
    }

    public Category getByIndex(Long id) {
        return categoryRepository.getCategoryById(id).orElseThrow();
    }
}
