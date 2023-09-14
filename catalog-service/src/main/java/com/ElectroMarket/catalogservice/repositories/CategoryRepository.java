package com.ElectroMarket.catalogservice.repositories;

import com.ElectroMarket.catalogservice.models.Category;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query("SELECT * FROM category WHERE name = :name")
    Optional<Category> findByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("DELETE FROM category WHERE name = :name")
    void deleteByName(@Param("name") String name);
}
