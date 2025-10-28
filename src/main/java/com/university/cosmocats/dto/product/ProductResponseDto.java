package com.university.cosmocats.dto.product;

import lombok.*;

import java.math.BigDecimal;

@Value
@Builder
public class ProductResponseDto {
    Long id;
    String name;
    String description;
    BigDecimal price;
    String category;
}
