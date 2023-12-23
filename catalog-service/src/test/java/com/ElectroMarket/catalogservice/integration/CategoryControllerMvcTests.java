package com.ElectroMarket.catalogservice.integration;

import com.ElectroMarket.catalogservice.config.SecurityConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
@Import(SecurityConfig.class)
public class CategoryControllerMvcTests {

    private static final String ROLE_EMPLOYEE = "ROLE_employee";
    private static final String ROLE_CUSTOMER = "ROLE_customer";
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    JwtDecoder jwtDecoder;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @Test
    void getExistingCategoryAndAuthenticatedThenShouldReturn200() throws Exception {
        var category = Category.of("category");
        given(categoryService.getCategoryById(4L)).willReturn(category);
        mockMvc
                .perform(get("/v1/category/4")
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    void getExistingCategoryAndNotAuthenticatedThenShouldReturn200() throws Exception {
        var category = Category.of("category");
        given(categoryService.getCategoryById(4L)).willReturn(category);
        mockMvc
                .perform(get("/v1/category/4"))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutCategoryWithEmployeeRoleThenShouldReturn200() throws Exception {
        var category = Category.of("category");
        when(categoryService.updateCategory(1L, category)).thenReturn(category);

        mockMvc.perform(put("/v1/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_EMPLOYEE))))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutCategoryWithCustomerRoleThenShouldReturn200() throws Exception {
        var category = Category.of("category");
        when(categoryService.updateCategory(1L, category)).thenReturn(category);

        mockMvc.perform(put("/v1/category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CUSTOMER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPutCategoryAndNotAuthenticatedThenShouldReturn401() throws Exception {
        var category = Category.of("category");
        when(categoryService.updateCategory(1L, category)).thenReturn(category);

        mockMvc.perform(put("/v1//category/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void whenDeleteCategoryNotAuthenticatedThenShouldReturn401() throws Exception   {
        var category = Category.of("category");
        given(categoryService.getCategoryById(1L)).willReturn(category);
        mockMvc.perform(delete("/v1/category/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getNonExistingCategoryThenShouldReturn404() throws Exception {
        given(categoryService.getCategoryById(5L))
                .willThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/v1/category/5"))
                .andExpect(status().isNotFound());
    }

    @Test
    void postExistingCategoryThenShouldReturn422() throws Exception {
        Category category = Category.of("category");

        String requestBody = new ObjectMapper().writeValueAsString(category);

        given(categoryService.createCategory(any(Category.class)))
                .willThrow(new ResourceAlreadyExistsException("category", category.id()));

        mockMvc.perform(
                        post("/v1/category")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_EMPLOYEE))))
                .andExpect(status().isUnprocessableEntity());
    }
}
