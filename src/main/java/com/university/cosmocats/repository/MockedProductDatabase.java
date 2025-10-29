package com.university.cosmocats.repository;

import com.university.cosmocats.model.product.Category;
import com.university.cosmocats.model.product.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MockedProductDatabase {
    private Map<Long, Product> products = new HashMap<>();

    public MockedProductDatabase() {
        initMockData();
    }

    private void initMockData() {
        Category electronics = Category.builder()
                .id(1L)
                .name("Electronics")
                .description("Electronic gadgets and devices")
                .build();

        Category books = Category.builder()
                .id(2L)
                .name("Books")
                .description("Printed and digital books")
                .build();

        Product iphone = Product.builder()
                .id(1L)
                .name("iPhone 15")
                .description("Latest Apple smartphone with A17 chip")
                .price(BigDecimal.valueOf(999.99))
                .category(electronics)
                .build();

        Product cleanCode = Product.builder()
                .id(2L)
                .name("Clean Code")
                .description("A Handbook of Agile Software Craftsmanship by Robert C. Martin")
                .price(BigDecimal.valueOf(45.50))
                .category(books)
                .build();

        products.put(iphone.getId(), iphone);
        products.put(cleanCode.getId(), cleanCode);
    }

    public Product save(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    public Product findById(Long id) {
        return products.get(id);
    }

    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public void deleteById(Long id) {
        products.remove(id);
    }
}
