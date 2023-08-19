package me.agronaut.springrest;

import me.agronaut.springrest.controller.CategoryController;
import me.agronaut.springrest.model.CategoryDto;
import me.agronaut.springrest.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringRestApplication.class)
@AutoConfigureMockMvc
class CategoryControllerTest {
    @Value("${ADMIN_TOKEN}")
    private String token;

    @Autowired
    private CategoryController controller;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void smokeTest(){
        assertNotNull(controller);
    }

    @Test
    void testGetAll() throws Exception {
        List<CategoryDto> res = new ArrayList<>();
        res.add(new CategoryDto(1L, "test1"));
        res.add(new CategoryDto(2L, "test2"));
        res.add(new CategoryDto(3L, "test3"));

        doReturn(res).when(categoryService).getAll();

        //without token
        mockMvc.perform(get("/category/get")).andExpect(status().isForbidden());

        //with token
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        mockMvc.perform(get("/category/get").headers(headers)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].name").value("test2"));
    }

    @Test
    void testAdd() throws Exception {
        CategoryDto mockDTO = new CategoryDto(1L, "asdasd");
        CategoryDto mockDTOSave = new CategoryDto(null, "asdasd");

        doReturn(mockDTO).when(categoryService).save(any(CategoryDto.class));

        //without token
        mockMvc.perform(post("/category/add").contentType(MediaType.APPLICATION_JSON).content(mockDTOSave.toJson()))
                .andExpect(status().isForbidden());

        //with token
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        mockMvc.perform(post("/category/add")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mockDTOSave.toJson())).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty());
    }
}
