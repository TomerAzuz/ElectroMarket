package com.ElectroMarket.catalogservice.integration;

import com.ElectroMarket.catalogservice.controllers.CategoryController;
import com.ElectroMarket.catalogservice.exceptions.ResourceAlreadyExistsException;
import com.ElectroMarket.catalogservice.exceptions.ResourceNotFoundException;
import com.ElectroMarket.catalogservice.models.Category;
import com.ElectroMarket.catalogservice.services.CategoryService;
import com.ElectroMarket.catalogservice.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerMvcTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @Test
    void getExistingCategory() throws Exception {
        var category = Category.of("category");
        given(categoryService.getCategoryById(4L)).willReturn(category);
        mockMvc.perform(get("/category/4"))
                .andExpect(status().isOk());
    }

    @Test
    void editExistingCategory() throws Exception {
        var category = Category.of("category");
        when(categoryService.updateCategory(1L, category)).thenReturn(category);

        mockMvc.perform(put("/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(category)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteExistingCategory() throws Exception   {
        var category = Category.of("category");
        given(categoryService.getCategoryById(1L)).willReturn(category);
        mockMvc.perform(delete("/category/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void categoryDoesNotExist() throws Exception {
        given(categoryService.getCategoryById(5L))
                .willThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/category/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void categoryAlreadyExists() throws Exception {
        Category category = Category.of("category");

        String requestBody = new ObjectMapper().writeValueAsString(category);

        mockMvc.perform(
                        post("/category")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated());

        given(categoryService.createCategory(any(Category.class)))
                .willThrow(new ResourceAlreadyExistsException("category", category.id()));

        mockMvc.perform(
                        post("/category")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isUnprocessableEntity());
    }
}
