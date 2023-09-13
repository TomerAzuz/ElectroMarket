package com.ElectroMarket.catalog.integration;

import com.ElectroMarket.catalog.controllers.CategoryController;
import com.ElectroMarket.catalog.exceptions.ResourceAlreadyExistsException;
import com.ElectroMarket.catalog.exceptions.ResourceNotFoundException;
import com.ElectroMarket.catalog.models.Category;
import com.ElectroMarket.catalog.services.CategoryService;
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

    @Test
    void getExistingCategory() throws Exception {
        var category = Category.of("category", null);
        given(categoryService.getCategoryById(4L)).willReturn(category);
        mockMvc.perform(get("/categories/4"))
                .andExpect(status().isOk());
    }

    @Test
    void editExistingCategory() throws Exception {
        var category = Category.of("category", null);
        when(categoryService.updateCategory(1L, category)).thenReturn(category);

        mockMvc.perform(put("/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(category)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteExistingCategory() throws Exception   {
        var category = Category.of("category", null);
        given(categoryService.getCategoryById(1L)).willReturn(category);
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void categoryDoesNotExist() throws Exception {
        given(categoryService.getCategoryById(5L))
                .willThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/categories/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void categoryAlreadyExists() throws Exception {
        Category category = Category.of("category", null);

        String requestBody = new ObjectMapper().writeValueAsString(category);

        mockMvc.perform(
                        post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated());

        given(categoryService.createCategory(any(Category.class)))
                .willThrow(new ResourceAlreadyExistsException("category", category.id()));

        mockMvc.perform(
                        post("/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isUnprocessableEntity());
    }

}
