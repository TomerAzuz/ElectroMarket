package com.ElectroMarket.catalogservice.integration;

import com.ElectroMarket.catalogservice.config.SecurityConfig;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
public class ProductControllerMvcTests {

    private static final String ROLE_EMPLOYEE = "ROLE_employee";
    private static final String ROLE_CUSTOMER = "ROLE_customer";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    JwtDecoder jwtDecoder;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @Test
    void getExistingProductAndAuthenticatedThenShouldReturn200() throws Exception {
        var product = Product.of(
                "product",
                BigDecimal.valueOf(9.90),
                1L,
                10,
                "https://example.com/image.jpg",
                "brand");
        given(productService.viewProductDetails(1L)).willReturn(product);
        mockMvc
                .perform(get("/v1/products/1")
                        .with(jwt()))
                .andExpect(status().isOk());
    }

    @Test void getExistingProductAndNotAuthenticatedThenShouldReturn200() throws Exception {
        var product = Product.of("product", BigDecimal.valueOf(9.90), 1L, 10,"https://example.com/image.jpg", "brand");
        given(productService.viewProductDetails(1L)).willReturn(product);
        mockMvc
                .perform(get("/v1/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getNonExistingProductAndNotAuthenticatedThenShouldReturn404() throws Exception {
        given(productService.viewProductDetails(1L))
                .willThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/v1/products/1"))
               .andExpect(status().isNotFound());
    }

    @Test
    void getNonExistingProductAndAuthenticatedThenShouldReturn404() throws Exception {
        given(productService.viewProductDetails(1L))
                .willThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/v1/products/1")
                        .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenDeleteProductWithEmployeeRoleThenShouldReturn204() throws Exception  {
        var productId = 1;
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/v1/products/" + productId)
                .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority("ROLE_employee"))))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
    
    @Test 
    void whenDeleteProductWithCustomerRoleThenShouldReturn403() throws Exception {
        var productId = 1;
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/v1/products/" + productId)
                        .with(SecurityMockMvcRequestPostProcessors.jwt()
                                .authorities(new SimpleGrantedAuthority("ROLE_customer"))))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }
    
    @Test 
    void whenDeleteProductNotAuthenticatedThenShouldReturn401() throws Exception {
        var productId = 1;
        mockMvc
                .perform(MockMvcRequestBuilders.delete("/v1/products/" + productId))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void whenPostProductWithEmployeeRoleThenShouldReturn201() throws Exception {
        var product = Product.of(
                "product",
                BigDecimal.valueOf(9.90),
                1L,
                10,
                "https://example.com/image.jpg",
                "brand");
        mockMvc
                .perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_EMPLOYEE))))
                        .andExpect(status().isCreated());
    }

    @Test
    void whenPostProductWithCustomerRoleThenShouldReturn201() throws Exception {
        var product = Product.of(
                "product",
                BigDecimal.valueOf(9.90),
                1L,
                10,
                "https://example.com/image.jpg",
                "brand");
        mockMvc
                .perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CUSTOMER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPutProductWithEmployeeRoleThenShouldReturn200() throws Exception {
        var productId = 1;
        var product = Product.of("product", BigDecimal.valueOf(9.90), 1L, 10,"https://example.com/image.jpg", "brand");
        given(productService.addProductToCatalog(product)).willReturn(product);
        mockMvc
                .perform(put("/v1/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_EMPLOYEE))))
                .andExpect(status().isOk());
    }

    @Test
    void whenPutProductWithCustomerRoleThenShouldReturn403() throws Exception {
        var productId = 1;
        var product = Product.of(
                "product",
                BigDecimal.valueOf(9.90),
                1L,
                10,
                "https://example.com/image.jpg",
                "brand");
        given(productService.addProductToCatalog(product)).willReturn(product);
        mockMvc
                .perform(put("/v1/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_CUSTOMER))))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenPutProductAndNotAuthenticatedThenShouldReturn401() throws Exception {
        var productId = 1;
        var product = Product.of(
                "product",
                BigDecimal.valueOf(9.90),
                1L,
                10,
                "https://example.com/image.jpg",
                "brand");
        given(productService.addProductToCatalog(product)).willReturn(product);
        mockMvc
                .perform(put("/v1/products/" + productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void PostExistingProduct() throws Exception {
        Product product = Product.of(
                "Sample Product",
                BigDecimal.valueOf(19.99),
                1L,
                10,
                "https://example.com/image.jpg",
                "brand"
        );

        String requestBody = new ObjectMapper().writeValueAsString(product);

        mockMvc
                .perform(post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product))
                        .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_EMPLOYEE))))
                .andExpect(status().isCreated());

        given(productService.addProductToCatalog(any(Product.class)))
                .willThrow(new ResourceAlreadyExistsException("product", product.id()));

        mockMvc.perform(
                        post("/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                                .with(jwt().authorities(new SimpleGrantedAuthority(ROLE_EMPLOYEE))))
                .andExpect(status().isUnprocessableEntity());
    }
}
