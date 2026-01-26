package com.university.cosmocats.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Builder
@Value
public class CreateCategoryRequestDto {
    @NotBlank(message = "Category name is mandatory field")
    @Size(min = 3, max = 255, message = "Category name should be between 3 and 255 characters")
    String name;

    @Size(min = 3, max = 255, message = "Category description should be between 3 and 255 characters")
    String description;

}
