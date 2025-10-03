package com.university.cosmocats.model.cart;

import com.university.cosmocats.model.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// This class is a future entity
public class CartItem {
    private Long id;
    private Product product;
    private Integer quantity;

    //one-to-many relation
    private ShoppingCart shoppingCart;
}

