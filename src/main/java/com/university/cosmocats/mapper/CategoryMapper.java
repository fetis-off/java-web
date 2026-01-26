package com.university.cosmocats.mapper;

import com.university.cosmocats.domain.product.Category;
import com.university.cosmocats.dto.category.CategoryResponseDto;
import com.university.cosmocats.dto.category.CreateCategoryRequestDto;
import com.university.cosmocats.dto.category.ProductCategoryData;
import com.university.cosmocats.dto.product.ProductShortInfoDto;
import com.university.cosmocats.entity.CategoryEntity;
import com.university.cosmocats.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryEntity toCategoryEntity(CreateCategoryRequestDto requestDto);


    @Mapping(target = "categoryInfo", source = ".")
    @Mapping(target = "productShortInfo", source = "products")
    ProductCategoryData toProductCategoryData(CategoryEntity categoryEntity);

    @Mapping(target = "productId", source = "id")
    @Mapping(target = "productName", source = "name")
    ProductShortInfoDto toProductShortInfoDto(ProductEntity productEntity);

    CategoryResponseDto toCategoryResponseDto(CategoryEntity categoryEntity);

    void updateCategory(@MappingTarget CategoryEntity categoryToUpdate, CreateCategoryRequestDto requestDto);
}
