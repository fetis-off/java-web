package com.university.cosmocats.dto.orderitem;

import com.university.cosmocats.dto.product.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private Integer quantity;
    private ProductResponseDto product;
}
