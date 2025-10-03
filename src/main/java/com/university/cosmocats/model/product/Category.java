package com.university.cosmocats.model.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// This class is a future entity
public class Category {
    private Long id;
    private String name;
    private String description;
}
