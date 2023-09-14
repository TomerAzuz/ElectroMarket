package com.ElectroMarket.catalogservice.repositories;

import com.ElectroMarket.catalogservice.models.Product;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("SELECT * FROM product WHERE name = :name")
    List<Product> findByName(@Param("name") String name);
    @Query("SELECT * FROM product WHERE price <= :maxPrice")
    List<Product> findProductsByPriceLessThan(@Param("maxPrice") Double maxPrice);

    @Query("SELECT * FROM product WHERE price >= :minPrice")
    List<Product> findProductsByPriceGreaterThan(@Param("minPrice") Double minPrice);

    @Query("SELECT p FROM Product p WHERE p.category.id = :id")
    List<Product> findByCategoryId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM product WHERE name = :name")
    void deleteByName(@Param("name")  String name);
}

