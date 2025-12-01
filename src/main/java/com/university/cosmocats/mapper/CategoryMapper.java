package com.university.cosmocats.mapper;

import com.university.cosmocats.dto.category.CategoryResponseDto;
import com.university.cosmocats.dto.category.CreateCategoryRequestDto;
import com.university.cosmocats.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryEntity toCategoryEntity(CreateCategoryRequestDto requestDto);

    CategoryResponseDto toCategoryResponseDto(CategoryEntity categoryEntity);

    void updateCategory(@MappingTarget CategoryEntity categoryToUpdate, CreateCategoryRequestDto requestDto);
}
