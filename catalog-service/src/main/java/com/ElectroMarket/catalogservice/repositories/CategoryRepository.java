package com.ElectroMarket.catalogservice.repositories;

import com.ElectroMarket.catalogservice.models.Category;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query("UPDATE Category SET name = :newName WHERE name = :name")
    int updateByName(@Param("name") String name, @Param("newName") String newName);


    @Modifying
    @Transactional
    @Query("DELETE FROM category WHERE name = :name")
    void deleteByName(@Param("name") String name);
}
