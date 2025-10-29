package com.university.cosmocats.mapper.product;

import com.university.cosmocats.dto.product.CreateProductRequestDto;
import com.university.cosmocats.dto.product.ProductResponseDto;
import com.university.cosmocats.model.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category", target = "category", ignore = true)
    Product toProduct(CreateProductRequestDto requestDto);

    @Mapping(source = "category.name", target = "category")
    ProductResponseDto toProductResponseDto(Product product);

    @Mapping(source = "category", target = "category", ignore = true)
    void updateProduct(@MappingTarget Product product, CreateProductRequestDto requestDto);
}
