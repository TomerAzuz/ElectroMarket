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

    @Query("SELECT * FROM product WHERE category_id = :id")
    List<Product> findProductsByCategory(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query("DELETE FROM product WHERE name = :name")
    void deleteByName(@Param("name")  String name);
}

