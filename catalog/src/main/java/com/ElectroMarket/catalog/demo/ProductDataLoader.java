package com.ElectroMarket.catalog.demo;

import com.ElectroMarket.catalog.models.Product;
import com.ElectroMarket.catalog.repositories.ProductRepository;
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
        var product1 = Product.of("product1", "some product.", 9.90, 1L, 10, "https://example.com/image.jpg");
        var product2 = Product.of("product2", "some product.", 199.99, 1L, 10, "https://example.com/image2 .jpg");
        productRepository.saveAll(List.of(product1, product2));
    }
}
