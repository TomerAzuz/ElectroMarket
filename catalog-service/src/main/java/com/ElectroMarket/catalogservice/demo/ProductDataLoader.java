package com.ElectroMarket.catalogservice.demo;

import com.ElectroMarket.catalogservice.models.Product;
import com.ElectroMarket.catalogservice.repositories.ProductRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("testdata")
public class ProductDataLoader {
    private final ProductRepository productRepository;

    public ProductDataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadProductTestData()   {
        productRepository.deleteAll();
        var product1 = Product.of("Laptop", "Some description1.", 559.90, 1L, 102, "https://example.com/image.jpg");
        var product2 = Product.of("Camera", "Some description2.", 399.99, 2L, 97, "https://example.com/image2 .jpg");
        var product3 = Product.of("TV", "Some description3.", 699.99, 3L, 57, "https://example.com/image3.jpg");
        productRepository.saveAll(List.of(product1, product2, product3));
    }
}
