package com.university.cosmocats.dto.product;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@AllArgsConstructor
@Builder
public class UpdateProductRequestDto {
    @NotBlank(message = "Name is mandatory field")
    @Size(min = 3, max = 100, message = "Name should be between 3 and 100 characters")
    String name;

    @NotBlank(message = "Description is mandatory field")
    @Size(min = 10, max = 255, message = "Description should be between 10 and 255 characters")
    String description;

    @NotNull(message = "Price is mandatory field")
    @DecimalMin(value = "0.01", message = "Price should be greater than 0")
    BigDecimal price;
}
