package com.ElectroMarket.catalogservice.demo;

import com.ElectroMarket.catalogservice.models.Category;
import com.ElectroMarket.catalogservice.models.Product;
import com.ElectroMarket.catalogservice.repositories.CategoryRepository;
import com.ElectroMarket.catalogservice.repositories.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Profile("testdata")
public class DataLoader {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataLoader(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadCategoryTestData()  {
        categoryRepository.deleteAll();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("category.json");
            if (resourceStream != null) {
                List<Category> categories = objectMapper.readValue(resourceStream, new TypeReference<List<Category>>() {});
                categoryRepository.saveAll(categories);
                loadProductTestData();
            } else {
                System.err.println("Unable to load category.json.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadProductTestData() {
        productRepository.deleteAll();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream resourceStream = getClass().getClassLoader().getResourceAsStream("products.json");

            if (resourceStream != null) {
                List<Product> products = objectMapper.readValue(resourceStream, new TypeReference<List<Product>>() {});

                productRepository.saveAll(products);
            } else {
                System.err.println("Unable to load products.json.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
