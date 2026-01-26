package com.university.cosmocats.dto.order;

import com.university.cosmocats.domain.order.OrderStatus;
import com.university.cosmocats.dto.customer.CustomerDto;
import com.university.cosmocats.dto.orderitem.OrderItemResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Long id;
    private CustomerDto customerDetails;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> orderItems;
}

