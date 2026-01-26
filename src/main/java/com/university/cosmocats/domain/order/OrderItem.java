package com.university.cosmocats.domain.order;

import com.university.cosmocats.domain.cart.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderItem {
    private Long id;

    // many-to-one relation
    private List<CartItem> cartItems;

    private Integer price;

    //one-to-many relation
    private Order order;
}
