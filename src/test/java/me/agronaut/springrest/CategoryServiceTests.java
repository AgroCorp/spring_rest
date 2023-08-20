package me.agronaut.springrest;

import me.agronaut.springrest.model.Category;
import me.agronaut.springrest.model.CategoryDto;
import me.agronaut.springrest.repository.CategoryRepository;
import me.agronaut.springrest.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringRestApplication.class)
class CategoryServiceTests {
    @MockBean
    private CategoryRepository repository;

    @InjectMocks @Autowired
    private CategoryService categoryService;

    private final Category mockCat = new Category(5L, "test");

    @Test
    void testGetByIndex() {
        doReturn(Optional.of(mockCat)).when(repository).getCategoryById(anyLong());

        assertThrows(NoSuchElementException.class, ()->{categoryService.getByIndex(null);});
        categoryService.getByIndex(5L);

        verify(repository, times(2)).getCategoryById(nullable(Long.class));
    }

    @Test
    void testGetAll() {
        List<Category> res = new ArrayList<>();
        res.add(new Category(1L, "test"));
        res.add(new Category(2L, "test2"));
        res.add(new Category(3L, "test3"));

        doReturn(res).when(repository).findAll();


        assertDoesNotThrow(()->{
            categoryService.getAll();
        });

        verify(repository, times(1)).findAll();

    }

    @Test
    void testSave() {
        doReturn(mockCat).when(repository).save(any(Category.class));

        assertThrows(NoSuchElementException.class, ()-> {
            categoryService.save(null);
        });
        categoryService.save(new CategoryDto());
        categoryService.save(new CategoryDto(null, "asdasd"));
        categoryService.save(new CategoryDto(1L, null));
        categoryService.save(new CategoryDto(3L, "asdasd"));

        verify(repository, times(4)).save(any(Category.class));
    }

    @Test
    void testDelete() {
        CategoryDto empty = new CategoryDto();
        CategoryDto nullId = new CategoryDto(null, "asdasd");

        doNothing().when(repository).delete(any(Category.class));

        assertThrows(NoSuchElementException.class, () -> {
            categoryService.delete(null);
        });
        assertThrows(NoSuchElementException.class, () -> {
            categoryService.delete(empty);
        });
        assertThrows(NoSuchElementException.class, () -> {
            categoryService.delete(nullId);
        });
        categoryService.delete(new CategoryDto(1L, null));
        categoryService.delete(new CategoryDto(2L, "asdasd"));

        verify(repository, times(2)).delete(any(Category.class));
    }
}
