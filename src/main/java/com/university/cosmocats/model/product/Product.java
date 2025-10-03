package com.university.cosmocats.model.product;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
// This class is a future entity
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer quantity;

    //many-to-one relation
    private Category category;
}
