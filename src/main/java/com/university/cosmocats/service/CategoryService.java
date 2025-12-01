package com.university.cosmocats.service;

import com.university.cosmocats.dto.category.CategoryResponseDto;
import com.university.cosmocats.dto.category.CreateCategoryRequestDto;
import com.university.cosmocats.entity.CategoryEntity;
import com.university.cosmocats.exception.CategoryNotFoundException;
import com.university.cosmocats.mapper.CategoryMapper;
import com.university.cosmocats.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponseDto createCategory(CreateCategoryRequestDto requestDto) {
        log.info("Creating a new category: {}", requestDto);
        CategoryEntity savedCategory = categoryRepository.save(categoryMapper.toCategoryEntity(requestDto));
        return categoryMapper.toCategoryResponseDto(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponseDto findCategoryById(Long id) {
        log.info("Trying to find category by id: {}", id);
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id +" was not found"));

        return categoryMapper.toCategoryResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAllCategories() {
        log.info("Finding all categories");
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponseDto)
                .toList();
    }

    @Transactional
    public CategoryResponseDto updateCategory(Long id, CreateCategoryRequestDto updateRequestDto) {
        log.info("Updating category: {}", updateRequestDto);

        CategoryEntity existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category with id: " + id +" was not found"));

        categoryMapper.updateCategory(existingCategory, updateRequestDto);

        return categoryMapper.toCategoryResponseDto(existingCategory);
    }

    @Transactional
    public void deleteCategoryById(Long id) {
        log.info("Deleting category with id: {}", id);
        categoryRepository.deleteById(id);
    }

}
