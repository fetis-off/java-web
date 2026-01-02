package com.university.cosmocats.mapper;

import com.university.cosmocats.dto.product.ProductRequestDto;
import com.university.cosmocats.dto.product.ProductResponseDto;
import com.university.cosmocats.dto.product.UpdateProductRequestDto;
import com.university.cosmocats.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductEntity toProductEntity(ProductRequestDto requestDto);

    ProductResponseDto toProductResponseDto(ProductEntity productEntity);

    void updateProduct(@MappingTarget ProductEntity productEntity, UpdateProductRequestDto requestDto);
}
