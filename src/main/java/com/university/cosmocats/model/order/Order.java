package com.university.cosmocats.model.order;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
// This class is a future entity
public class Order {
    private Long id;

    //many-to-one relation
    private List<OrderItem> orderItems;

    private BigDecimal totalPrice;

    private LocalDate orderDate;

    private String shippingAddress;

    private OrderStatus status;
}
