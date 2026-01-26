package com.university.cosmocats.repository;

import com.university.cosmocats.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> getProductEntitiesByIdIn(Collection<Long> productIds);
}
