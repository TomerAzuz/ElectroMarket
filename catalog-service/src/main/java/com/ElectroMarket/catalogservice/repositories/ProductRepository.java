package com.ElectroMarket.catalogservice.repositories;

import com.ElectroMarket.catalogservice.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long>, CrudRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(@Param("query") String query, Pageable pageable);

    Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);

    @Modifying
    @Transactional
    @Query("DELETE FROM product WHERE name = :name")
    void deleteByName(@Param("name")  String title);
}

