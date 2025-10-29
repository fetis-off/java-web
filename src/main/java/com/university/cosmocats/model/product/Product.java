package com.university.cosmocats.model.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;

    //many-to-one relation
    private Category category;
}
