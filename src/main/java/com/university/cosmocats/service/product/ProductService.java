package com.university.cosmocats.service.product;

import com.university.cosmocats.dto.product.CreateProductRequestDto;
import com.university.cosmocats.exception.ProductNotFoundException;
import com.university.cosmocats.mapper.product.ProductMapper;
import com.university.cosmocats.model.product.Product;
import com.university.cosmocats.repository.MockedProductDatabase;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ProductService {
    private final MockedProductDatabase mockedProductDatabase;
    private final ProductMapper productMapper;

    public ProductService(MockedProductDatabase mockedProductDatabase, ProductMapper productMapper) {
        this.mockedProductDatabase = mockedProductDatabase;
        this.productMapper = productMapper;
    }

    public Product createProduct(CreateProductRequestDto requestDto) {
        Long id = generateId();
        Product product = productMapper.toProduct(requestDto);
        product.setId(id);
        return mockedProductDatabase.save(product);
    }

    public List<Product> getAllProducts() {
        return mockedProductDatabase.findAll();
    }

    public Product getProductById(Long id) {
        Product product = mockedProductDatabase.findById(id);
        if (Objects.isNull(product)) {
            throw new ProductNotFoundException("Product with ID: " + id + " not found");
        }
        return product;
    }

    public void deleteProduct(Long id) {
        if (Objects.isNull(mockedProductDatabase.findById(id))) {
            throw new ProductNotFoundException("Product with ID: " + id + " not found");
        }
        mockedProductDatabase.deleteById(id);
    }

    public Product updateProduct(Long id, CreateProductRequestDto requestDto) {
        Product existingProduct = getProductById(id);
        productMapper.updateProduct(existingProduct, requestDto);
        return mockedProductDatabase.save(existingProduct);
    }

    private Long generateId() {
        return mockedProductDatabase.findAll().stream()
                .mapToLong(Product::getId)
                .max()
                .orElse(0L) + 1;
    }

}
