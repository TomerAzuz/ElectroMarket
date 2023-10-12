package com.ElectroMarket.catalogservice.integration;

import com.ElectroMarket.catalogservice.config.DataConfig;
import com.ElectroMarket.catalogservice.models.Product;
import com.ElectroMarket.catalogservice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
public class ProductRepositoryJdbcTests {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;


    @Test
    void findAllProducts()  {
        var prod1 = Product.of("Keyboard", "some keyboard", 29.99, 1L, 10, "https://example.com/image.jpg");
        var prod2 = Product.of("headphones", "description", 99.99, 2L, 10, "https://example.com/image2.jpg");

        jdbcAggregateTemplate.insert(prod1);
        jdbcAggregateTemplate.insert(prod2);

        Iterable<Product> actualProducts = productRepository.findAll();

        assertThat(StreamSupport.stream(actualProducts.spliterator(), true)
                .filter(product -> product.name().equals(prod1.name()) ||
                                   product.name().equals(prod2.name()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findProductsByCategory()   {
        List<Product> products = List.of(
                Product.of("Keyboard1", "some keyboard1", 29.99, 1L, 110, "https://example.com/image.jpg"),
                Product.of("Headphones", "description", 92.99, 2L, 40, "https://example.com/image2.jpg"),
                Product.of("Keyboard2", "some keyboard2", 70.0, 1L, 15, "https://example.com/image2.jpg")
        );
        jdbcAggregateTemplate.insertAll(products);
        List<Product> actualProducts = productRepository.findProductsByCategory(1L);

        assertThat(actualProducts.parallelStream()
                .filter(product -> product.name().equals("Keyboard1") ||
                        product.name().equals("Keyboard2"))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findExistingProductByName()  {
        var prod = Product.of("T-shirt", "some t-shirt", 7.99, 2L, 10, "https://example.com/image.jpg");
        jdbcAggregateTemplate.insert(prod);
        List<Product> actualProducts = productRepository.findByName("T-shirt");
        assertThat(actualProducts).isNotEmpty();
        assertThat(actualProducts.get(0).name()).isEqualTo(prod.name());
    }

    @Test
    void findNonExistingProductByName() {
        List<Product> actualProducts = productRepository.findByName("NonExistingProduct");
        assertThat(actualProducts).isEmpty();
    }

    @Test
    void saveProduct()  {
        var prod1 = Product.of("Laptop", "description", 299.99, 1L, 10, "https://example.com/image.jpg");

        Product savedProduct = productRepository.save(prod1);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.id()).isNotNull();
        assertThat(savedProduct.name()).isEqualTo(prod1.name());
    }

    @Test
    void deleteProduct() {
        var product = Product.of("Tablet", "description", 299.99, 1L, 5, "https://example.com/image.jpg");
        jdbcAggregateTemplate.insert(product);
        productRepository.deleteByName(product.name());
        List<Product> deletedProduct = productRepository.findByName("Tablet");
        assertThat(deletedProduct).isEmpty();
    }
}
