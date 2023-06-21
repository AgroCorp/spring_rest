package me.agronaut.springrest.controller;

import me.agronaut.springrest.model.CategoryDto;
import me.agronaut.springrest.service.CategoryService;
import me.agronaut.springrest.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    private final LogUtil logger = new LogUtil(this.getClass());

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/get")
    public List<CategoryDto> getAll() {
        logger.debug("getAll - START");
        return categoryService.getAll();
    }

    @PostMapping("/add")
    public CategoryDto add(@RequestBody CategoryDto newCategory) {
        return categoryService.save(newCategory);
    }
}
