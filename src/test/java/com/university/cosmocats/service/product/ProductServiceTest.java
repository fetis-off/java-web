package com.university.cosmocats.service.product;

import com.university.cosmocats.config.MappersTestConfiguration;
import com.university.cosmocats.dto.product.CreateProductRequestDto;
import com.university.cosmocats.exception.ProductNotFoundException;
import com.university.cosmocats.model.product.Category;
import com.university.cosmocats.model.product.Product;
import com.university.cosmocats.repository.MockedProductDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ProductService.class)
@Import(MappersTestConfiguration.class)
@DisplayName("Product service tests")
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private static final Long CATEGORY_ID = 10L;
    private static final String CATEGORY_NAME = "Food";
    private static final String CATEGORY_DESCRIPTION = "Food category contains food";


    private static final Long VALID_PRODUCT_ID = 1L;
    private static final Long NON_EXISTING_PRODUCT_ID = 10000L;
    private static final String PRODUCT_NAME = "Banana";
    private static final String PRODUCT_DESCRIPTION = "Banana is a yellow fruit";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(15.99);

    private Product testProduct;
    private Category testCategory;
    private CreateProductRequestDto productRequestDto;

    @MockitoBean
    private MockedProductDatabase mockedProductDatabase;

    @Autowired
    private ProductService productService;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .description(CATEGORY_DESCRIPTION)
                .build();

        testProduct = Product.builder()
                .id(VALID_PRODUCT_ID)
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .category(testCategory)
                .build();

        productRequestDto = CreateProductRequestDto.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .category(CATEGORY_NAME)
                .build();
    }


    @Test
    @DisplayName("Should create product successfully")
    void testCreateProduct() {
        when(mockedProductDatabase.findAll()).thenReturn(List.of());
        when(mockedProductDatabase.save(any(Product.class))).thenReturn(testProduct);

        Product createdProduct = productService.createProduct(productRequestDto);

        assertNotNull(createdProduct);

        assertEquals(PRODUCT_NAME, createdProduct.getName());
        assertEquals(PRODUCT_DESCRIPTION, createdProduct.getDescription());
        assertEquals(CATEGORY_NAME, createdProduct.getCategory().getName());
        assertEquals(CATEGORY_DESCRIPTION, createdProduct.getCategory().getDescription());
        assertEquals(PRODUCT_PRICE, createdProduct.getPrice());
    }

    @Test
    @DisplayName("Should get product by existing id")
    void testGetExistingProductById() {
        when(mockedProductDatabase.findById(VALID_PRODUCT_ID)).thenReturn(testProduct);

        Product product = productService.getProductById(VALID_PRODUCT_ID);

        assertNotNull(product);

        assertEquals(PRODUCT_NAME, product.getName());
        assertEquals(PRODUCT_DESCRIPTION, product.getDescription());
        assertEquals(CATEGORY_NAME, product.getCategory().getName());
        assertEquals(CATEGORY_DESCRIPTION, product.getCategory().getDescription());
        assertEquals(PRODUCT_PRICE, product.getPrice());
    }

    @Test
    @DisplayName("Should get product by non-existing id")
    void testGetNonExistingProductById() {
        when(mockedProductDatabase.findById(NON_EXISTING_PRODUCT_ID)).thenReturn(null);

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(NON_EXISTING_PRODUCT_ID)
        );

        assertEquals("Product with ID: " + NON_EXISTING_PRODUCT_ID + " not found", exception.getMessage());
        verify(mockedProductDatabase).findById(NON_EXISTING_PRODUCT_ID);
    }

    @Test
    @DisplayName("Should generate ID 1 when database is empty")
    void testGenerateIdOneWhenDatabaseIsEmpty() {
        when(mockedProductDatabase.findAll()).thenReturn(Collections.emptyList());
        when(mockedProductDatabase.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product createdProduct = productService.createProduct(productRequestDto);

        assertNotNull(createdProduct);
        assertEquals(1L, createdProduct.getId());
    }

    @Test
    @DisplayName("Should generate next sequential ID")
    void testGenerateIdNextSequentialId() {
        Product product1 = Product.builder().id(5L).build();
        Product product2 = Product.builder().id(6L).build();
        when(mockedProductDatabase.findAll()).thenReturn(List.of(product1, product2));
        when(mockedProductDatabase.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Product createdProduct = productService.createProduct(productRequestDto);

        assertEquals(7L, createdProduct.getId());
    }

    @Test
    @DisplayName("Should get all products")
    void testGetAllProducts() {
        Product secondTestProduct = Product.builder().id(2L).build();
        List<Product> products = List.of(testProduct, secondTestProduct);
        when(mockedProductDatabase.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockedProductDatabase).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void testGetAllProductsWhenNoProductsExist() {
        when(mockedProductDatabase.findAll()).thenReturn(Collections.emptyList());

        List<Product> result = productService.getAllProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(mockedProductDatabase).findAll();
    }

    @Test
    @DisplayName("Should delete product successfully when ID exists")
    void testDeleteProductWhenIdExists() {
        when(mockedProductDatabase.findById(VALID_PRODUCT_ID)).thenReturn(testProduct);
        doNothing().when(mockedProductDatabase).deleteById(VALID_PRODUCT_ID);

        productService.deleteProduct(VALID_PRODUCT_ID);

        verify(mockedProductDatabase).findById(VALID_PRODUCT_ID);
        verify(mockedProductDatabase).deleteById(VALID_PRODUCT_ID);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent product")
    void testDeleteNonExistentProduct() {
        when(mockedProductDatabase.findById(NON_EXISTING_PRODUCT_ID)).thenReturn(null);

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.deleteProduct(NON_EXISTING_PRODUCT_ID)
        );

        assertEquals("Product with ID: " + NON_EXISTING_PRODUCT_ID + " not found", exception.getMessage());
        verify(mockedProductDatabase).findById(NON_EXISTING_PRODUCT_ID);
        verify(mockedProductDatabase, never()).deleteById(NON_EXISTING_PRODUCT_ID);
    }

    @Test
    @DisplayName("Should update product successfully when ID exists")
    void testUpdateProductWhenIdExists() {
        // Given
        CreateProductRequestDto updateRequest = CreateProductRequestDto.builder()
                        .name("Updated Banana")
                        .description("Updated description")
                        .price(BigDecimal.valueOf(19.99))
                        .build();

        when(mockedProductDatabase.findById(VALID_PRODUCT_ID)).thenReturn(testProduct);
        when(mockedProductDatabase.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = productService.updateProduct(VALID_PRODUCT_ID, updateRequest);

        assertNotNull(result);
        assertEquals(VALID_PRODUCT_ID, result.getId());
        verify(mockedProductDatabase).findById(VALID_PRODUCT_ID);
        verify(mockedProductDatabase).save(testProduct);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent product")
    void testUpdateNonExistentProduct() {
        CreateProductRequestDto updateRequest = CreateProductRequestDto.builder()
                .name("Updated Banana")
                .build();

        when(mockedProductDatabase.findById(NON_EXISTING_PRODUCT_ID)).thenReturn(null);

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(NON_EXISTING_PRODUCT_ID, updateRequest)
        );

        assertEquals("Product with ID: " + NON_EXISTING_PRODUCT_ID + " not found", exception.getMessage());
        verify(mockedProductDatabase).findById(NON_EXISTING_PRODUCT_ID);
        verify(mockedProductDatabase, never()).save(any());
    }

}