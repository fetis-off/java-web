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
        Category electronics = new Category();
        electronics.setId(1L);
        electronics.setName("Electronics");
        electronics.setDescription("Electronic gadgets and devices");

        Category books = new Category();
        books.setId(2L);
        books.setName("Books");
        books.setDescription("Printed and digital books");

        Product iphone = new Product();
        iphone.setId(1L);
        iphone.setName("iPhone 15");
        iphone.setDescription("Latest Apple smartphone with A17 chip");
        iphone.setPrice(BigDecimal.valueOf(999.99));
        iphone.setCategory(electronics);

        Product cleanCode = new Product();
        cleanCode.setId(2L);
        cleanCode.setName("Clean Code");
        cleanCode.setDescription("A Handbook of Agile Software Craftsmanship by Robert C. Martin");
        cleanCode.setPrice(BigDecimal.valueOf(45.50));
        cleanCode.setCategory(books);

        products.put(iphone.getId(), iphone);
        products.put(cleanCode.getId(), cleanCode);
    }

    public Product save(Product product) {
        return products.put(product.getId(), product);
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
