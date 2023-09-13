package com.ElectroMarket.catalog.integration;

import com.ElectroMarket.catalog.config.DataConfig;
import com.ElectroMarket.catalog.models.Product;
import com.ElectroMarket.catalog.repositories.ProductRepository;
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
        var prod1 = Product.of("T-shirt", "some t-shirt", 7.99, 2L, 10, "https://example.com/image.jpg");
        var prod2 = Product.of("headphones", "description", 99.99, 1L, 10, "https://example.com/image2.jpg");
        jdbcAggregateTemplate.insert(prod1);
        jdbcAggregateTemplate.insert(prod2);

        Iterable<Product> actualProducts = productRepository.findAll();

        assertThat(StreamSupport.stream(actualProducts.spliterator(), true)
                .filter(product -> product.name().equals(prod1.name()) || product.name().equals(prod2.name()))
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
    void findProductsByPriceGreaterThan() {
        var expensiveLaptop = Product.of("Expensive Laptop", "description", 1299.99, 1L, 10, "https://example.com/image.jpg");
        var affordableLaptop = Product.of("Affordable Laptop", "description", 799.99, 2L, 10, "https://example.com/image2.jpg");

        jdbcAggregateTemplate.insert(expensiveLaptop);
        jdbcAggregateTemplate.insert(affordableLaptop);

        List<Product> actualProducts = productRepository.findProductsByPriceGreaterThan(1000.0);

        assertThat(actualProducts.get(0).name()).isEqualTo(expensiveLaptop.name());
        assertThat(actualProducts).hasSize(1);
    }

    @Test
    void findProductsByPriceLessThan() {
        var expensiveLaptop = Product.of("Expensive Laptop", "description", 1299.99, 1L, 10, "https://example.com/image.jpg");
        var affordableLaptop = Product.of("Affordable Laptop", "description", 799.99, 2L, 10, "https://example.com/image2.jpg");

        jdbcAggregateTemplate.insert(expensiveLaptop);
        jdbcAggregateTemplate.insert(affordableLaptop);

        List<Product> actualProducts = productRepository.findProductsByPriceLessThan(1000.0);

        assertThat(actualProducts.get(0).name()).isEqualTo(affordableLaptop.name());
        assertThat(actualProducts).hasSize(1);
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
