package com.university.cosmocats.model.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
// This class is a future entity
public class ShoppingCart {
    private Long id;
    // many to one
    private List<CartItem> cartItems;
}
