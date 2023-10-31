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
    private CategoryRepository categoryRepository;

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;


    @Test
    void findAllProducts()  {
        Category category1 = categoryRepository.save(Category.of("Laptops"));
        Category category2 = categoryRepository.save(Category.of("Cameras"));

        var prod1 = new Product(1L,"Laptop", "description", 1129.99, category1.id(), 10,
                "https://example.com/image.jpg", null, null, 0);
        var prod2 = new Product(2L,"Camera", "description", 199.99, category2.id(), 10,
                "https://example.com/image2.jpg", null, null, 0);
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
                Product.of("Laptop1", "description", 1029.99, category1.id(), 110, "https://example.com/image.jpg"),
                Product.of("Camera", "description", 129.99, category2.id(), 40, "https://example.com/image2.jpg"),
                Product.of("Laptop2", "description", 760.0, category1.id(), 15, "https://example.com/image3.jpg")
        );
        jdbcAggregateTemplate.insertAll(products);
        var actualProducts = productRepository.findProductsByCategory(category1.id());

        assertThat(actualProducts.parallelStream()
                .filter(product -> product.name().equals("Laptop1") ||
                        product.name().equals("Laptop2"))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void findExistingProductByName()  {
        Category category = categoryRepository.save(Category.of("Laptops"));
        var prod = Product.of("Laptop", "description", 1129.99, category.id(), 10, "https://example.com/image.jpg");
        jdbcAggregateTemplate.insert(prod);
        List<Product> actualProducts = productRepository.findByName("Laptop");
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
        Category category = categoryRepository.save(Category.of("Laptops"));
        var prod1 = Product.of("Laptop", "description", 1129.99, category.id(), 10, "https://example.com/image.jpg");

        Product savedProduct = productRepository.save(prod1);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.id()).isNotNull();
        assertThat(savedProduct.name()).isEqualTo(prod1.name());
    }

    @Test
    void deleteProduct() {
        Category category = categoryRepository.save(Category.of("Laptops"));
        var product = Product.of("Laptop", "description", 299.99, category.id(), 5, "https://example.com/image.jpg");
        jdbcAggregateTemplate.insert(product);
        productRepository.deleteByName(product.name());
        List<Product> deletedProduct = productRepository.findByName("Laptop");
        assertThat(deletedProduct).isEmpty();
    }
}
