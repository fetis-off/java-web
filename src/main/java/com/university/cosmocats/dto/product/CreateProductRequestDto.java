package com.university.cosmocats.dto.product;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateProductRequestDto {
    @NotBlank(message = "Name is mandatory field")
    @Size(min = 3, max = 100, message = "Name should be between 3 and 100 characters")
    private String name;

    @NotBlank(message = "Description is mandatory field")
    @Size(min = 10, max = 255, message = "Description should be between 10 and 255 characters")
    private String description;

    @NotBlank(message = "Category is mandatory field")
    @Size(min = 3, max = 80, message = "Category should be between 3 and 80 characters")
    private String category;
    // p.s: In a future versions Category will be enum with same values as in database to validate it properly

    @NotNull(message = "Price is mandatory field")
    @DecimalMin(value = "0.01", message = "Price should be greater than 0")
    private BigDecimal price;

    @PositiveOrZero(message = "Quantity must be zero or a positive value")
    private Integer quantity;
}
