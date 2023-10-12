package com.ElectroMarket.catalogservice.demo;

import com.ElectroMarket.catalogservice.models.Product;
import com.ElectroMarket.catalogservice.repositories.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@Profile("testdata")
public class ProductDataLoader {
    private final ProductRepository productRepository;

    public ProductDataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
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
