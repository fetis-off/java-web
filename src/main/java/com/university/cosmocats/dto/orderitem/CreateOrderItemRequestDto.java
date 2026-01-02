package com.university.cosmocats.dto.orderitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder
public class CreateOrderItemRequestDto {
    @NotNull(message = "Product id is mandatory field")
    @Positive
    Long productId;

    @Positive(message = "Quantity should be a positive number")
    Integer quantity;
}
