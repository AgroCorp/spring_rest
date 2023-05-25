package me.agronaut.springrest;

import me.agronaut.springrest.model.Category;
import me.agronaut.springrest.repository.CategoryRepository;
import me.agronaut.springrest.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringRestApplication.class)
class CategoryServiceTests {
    @MockBean
    private CategoryRepository repository;

    @InjectMocks @Autowired
    private CategoryService categoryService;

    @Test
    void testGetByIndex() {
        Category mockCat = new Category();
        mockCat.setId(5L);
        doReturn(Optional.of(mockCat)).when(repository).getCategoryById(anyLong());

        assertThrows(NoSuchElementException.class, ()->{categoryService.getByIndex(null);});
        categoryService.getByIndex(5L);

        verify(repository, times(2)).getCategoryById(nullable(Long.class));

    }
}
