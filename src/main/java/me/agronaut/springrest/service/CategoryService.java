package me.agronaut.springrest.service;

import me.agronaut.springrest.model.Category;
import me.agronaut.springrest.model.CategoryDto;
import me.agronaut.springrest.repository.CategoryRepository;
import me.agronaut.springrest.util.LogUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final LogUtil log = new LogUtil(getClass());

    @Autowired
    public CategoryService(CategoryRepository repository, ModelMapper modelMapper) {
        this.categoryRepository = repository;
        this.modelMapper = modelMapper;
    }

    public Category getByIndex(Long id) {
        log.debug("getByIndex - START");
        return categoryRepository.getCategoryById(id).orElseThrow();
    }

    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
    }
    public CategoryDto save(CategoryDto cat) {
        log.debug("save - START");
        Category saved = categoryRepository.save(modelMapper.map(cat, Category.class));
        return modelMapper.map(saved,CategoryDto.class);
    }

    public void delete(CategoryDto cat) {
        log.debug("delete - START");
        categoryRepository.delete(modelMapper.map(cat, Category.class));
    }
}
