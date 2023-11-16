package com.ElectroMarket.catalogservice.integration;

import com.ElectroMarket.catalogservice.config.DataConfig;
import com.ElectroMarket.catalogservice.models.Category;
import com.ElectroMarket.catalogservice.models.Product;
import com.ElectroMarket.catalogservice.repositories.CategoryRepository;
import com.ElectroMarket.catalogservice.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("integration")
public class ProductRepositoryJdbcTests {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;


    @Test
    void findAllProducts()  {
        Category category1 = categoryRepository.save(Category.of("Laptops"));
        Category category2 = categoryRepository.save(Category.of("Cameras"));

        var prod1 = new Product(1L,"Laptop", 1129.99, category1.id(), 10,
                "https://example.com/image.jpg", "brand", null, null, null, null, 0);
        var prod2 = new Product(2L,"Camera", 199.99, category2.id(), 10,
                "https://example.com/image2.jpg", "brand", null, null, null, null, 0);
        var products = List.of(prod1, prod2);

        jdbcAggregateTemplate.insertAll(products);

        Iterable<Product> actualProducts = productRepository.findAll();

        assertThat(StreamSupport.stream(actualProducts.spliterator(), true)
                .filter(product -> product.name().equals(prod1.name()) ||
                                   product.name().equals(prod2.name()))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findProductsByCategory()   {
        Category category1 = categoryRepository.save(Category.of("Laptops"));
        Category category2 = categoryRepository.save(Category.of("Cameras"));
        var products = List.of(
                Product.of("Laptop1", 1029.99, category1.id(), 110, "https://example.com/image.jpg", "brand"),
                Product.of("Camera", 129.99, category2.id(), 40, "https://example.com/image2.jpg", "brand"),
                Product.of("Laptop2", 760.0, category1.id(), 15, "https://example.com/image3.jpg", "brand")
        );
        jdbcAggregateTemplate.insertAll(products);
        Pageable pageable = PageRequest.of(0, 10);
        var actualProducts = productRepository.findByCategoryId(category1.id(), pageable);

        assertThat(actualProducts.getContent().stream()
                .filter(product -> product.name().equals("Laptop1") ||
                        product.name().equals("Laptop2"))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findExistingProductByName()  {
        Category category = categoryRepository.save(Category.of("Laptops"));

        var product = Product.of("Laptop", 1129.99, category.id(), 10, "https://example.com/image.jpg", "brand");

        jdbcAggregateTemplate.insert(product);
        Pageable pageable = PageRequest.of(0, 1);
        Page<Product> actualProducts = productRepository.findByNameContainingIgnoreCase("Laptop", pageable);

        assertThat(actualProducts).isNotEmpty();
        assertThat(actualProducts.getContent()).hasSize(1);
        assertThat(actualProducts.getContent().get(0).name()).isEqualTo("Laptop");
    }

    @Test
    void findNonExistingProductByName() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Product> actualProducts = productRepository.findByNameContainingIgnoreCase("NonExistingProduct", pageable);

        assertThat(actualProducts).isNotNull();
        assertThat(actualProducts).isEmpty();
    }

    @Test
    void saveProduct()  {
        Category category = categoryRepository.save(Category.of("Laptops"));
        var prod1 = Product.of("Laptop", 1129.99, category.id(), 10, "https://example.com/image.jpg", "brand");

        Product savedProduct = productRepository.save(prod1);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.id()).isNotNull();
        assertThat(savedProduct.name()).isEqualTo(prod1.name());
    }

    @Test
    void deleteProduct() {
        Category category = categoryRepository.save(Category.of("Laptops"));
        var product = Product.of("Laptop", 299.99, category.id(), 5, "https://example.com/image.jpg", "brand");

        jdbcAggregateTemplate.insert(product);
        Pageable pageable = PageRequest.of(0, 1);
        productRepository.deleteByName(product.name());

        Page<Product> deletedProduct = productRepository.findByNameContainingIgnoreCase("Laptop", pageable);

        assertThat(deletedProduct).isNotNull();
        assertThat(deletedProduct).isEmpty();
    }

    @Test
    void whenCreateProductNotAuthenticatedThenNoAuditMetadata() {
        Category category = categoryRepository.save(Category.of("Laptops"));
        var productToCreate = Product.of("Laptop", 299.99, category.id(), 5, "https://example.com/image.jpg", "brand");
        var createdProduct = productRepository.save(productToCreate);

        assertThat(createdProduct.createdBy()).isNull();
        assertThat(createdProduct.lastModifiedBy()).isNull();
    }
    @Test
    @WithMockUser("tomer")
    void whenCreateProductAuthenticatedThenAuditingMetadata()   {
        Category category = categoryRepository.save(Category.of("Laptops"));
        var productToCreate = Product.of("Laptop", 299.99, category.id(), 5, "https://example.com/image.jpg", "brand");
        var createdProduct = productRepository.save(productToCreate);

        assertThat(createdProduct.createdBy()).isEqualTo("tomer");
        assertThat(createdProduct.lastModifiedBy()).isEqualTo("tomer");
    }
}
