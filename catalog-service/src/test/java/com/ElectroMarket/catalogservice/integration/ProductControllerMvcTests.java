package com.ElectroMarket.catalogservice.integration;

import com.ElectroMarket.catalogservice.controllers.ProductController;
import com.ElectroMarket.catalogservice.exceptions.ResourceAlreadyExistsException;
import com.ElectroMarket.catalogservice.exceptions.ResourceNotFoundException;
import com.ElectroMarket.catalogservice.models.Product;
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

@WebMvcTest(ProductController.class)
public class ProductControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void getExistingProduct() throws Exception {
        var product = Product.of("product", "description", 9.90, 1L, 10,"https://example.com/image.jpg");
        given(productService.viewProductDetails(1L)).willReturn(product);
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void addNonExistingProduct() throws Exception {
        var product = Product.of("product", "description", 9.90, 1L, 10, "https://example.com/image.jpg");
        given(productService.addProductToCatalog(product)).willReturn(product);
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(product)))
                .andExpect(status().isCreated());
    }

    @Test
    void editExistingProduct() throws Exception {
        var product = Product.of("Updated Product", "Updated Description", 19.99, 1L, 10, "https://example.com/image2.jpg");

        when(productService.editProductDetails(1L, product)).thenReturn(product);

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(product)))
                        .andExpect(status().isOk());
    }

    @Test
    void deleteExistingProduct() throws Exception   {
        var product = Product.of( "product", "description", 9.90,  1L, 10, "https://example.com/image.jpg");
        given(productService.viewProductDetails(1L)).willReturn(product);
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void productDoesNotExist() throws Exception {
        given(productService.viewProductDetails(1L))
                .willThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/api/products/1"))
               .andExpect(status().isNotFound());
    }

    @Test
    void productAlreadyExists() throws Exception {
        Product product = Product.of(
                "Sample Product",
                "This is a sample product description.",
                19.99,
                1L,
                10,
                "https://example.com/image.jpg"
        );

        String requestBody = new ObjectMapper().writeValueAsString(product);

        mockMvc.perform(
                        post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isCreated());

        given(productService.addProductToCatalog(any(Product.class)))
                .willThrow(new ResourceAlreadyExistsException("product", product.id()));

        mockMvc.perform(
                        post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpect(status().isUnprocessableEntity());
    }
}
