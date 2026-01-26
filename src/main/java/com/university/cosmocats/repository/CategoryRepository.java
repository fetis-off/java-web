package com.university.cosmocats.repository;

import com.university.cosmocats.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query("""
    SELECT c FROM CategoryEntity c
    LEFT JOIN FETCH ProductEntity p
    ON c.id = p.category.id
    WHERE c.id = :id
""")
    Optional<CategoryEntity> findByIdWithProducts(Long id);
}
