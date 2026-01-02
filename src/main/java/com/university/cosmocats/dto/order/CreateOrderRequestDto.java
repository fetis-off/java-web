package com.university.cosmocats.dto.order;

import com.university.cosmocats.dto.orderitem.CreateOrderItemRequestDto;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.List;

@Data
@Value
@Builder
public class CreateOrderRequestDto {
    @NotNull(message = "Customer id is mandatory field")
    @Positive
    Long customerId;

    @NotEmpty(message = "Request should contain order items")
    List<CreateOrderItemRequestDto> orderItemsListDto;
}
