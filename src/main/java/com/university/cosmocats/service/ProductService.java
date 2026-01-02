package com.university.cosmocats.service;

import com.university.cosmocats.dto.product.ProductRequestDto;
import com.university.cosmocats.dto.product.ProductResponseDto;
import com.university.cosmocats.dto.product.UpdateProductRequestDto;
import com.university.cosmocats.entity.CategoryEntity;
import com.university.cosmocats.entity.ProductEntity;
import com.university.cosmocats.exception.ProductNotFoundException;
import com.university.cosmocats.mapper.ProductMapper;
import com.university.cosmocats.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto requestDto) {
        CategoryEntity categoryEntity = categoryService.findCategoryEntityById(requestDto.getCategoryId());

        ProductEntity product = productMapper.toProductEntity(requestDto);
        product.setCategory(categoryEntity);
        productRepository.save(product);

        return productMapper.toProductResponseDto(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto getProductById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " was not found"));

        return productMapper.toProductResponseDto(productEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponseDto> getALlProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toProductResponseDto);
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, UpdateProductRequestDto updateRequestDto) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " was not found"));

        productMapper.updateProduct(productEntity, updateRequestDto);

        return productMapper.toProductResponseDto(productEntity);
    }

    @Transactional(readOnly = true)
    public ProductEntity getProductEntityById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id: " + id + " was not found"));
    }

    @Transactional(readOnly = true)
    public List<ProductEntity> getListOfProductsByIds(List<Long> ids) {
        return productRepository.getProductEntitiesByIdIn(ids);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
