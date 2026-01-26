package com.university.cosmocats.dto.category;

import com.university.cosmocats.dto.product.ProductShortInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryData {
    private CategoryResponseDto categoryInfo;
    private List<ProductShortInfoDto> productShortInfo;
}
