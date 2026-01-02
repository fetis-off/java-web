package com.university.cosmocats.service.product;

import com.university.cosmocats.dto.product.ProductRequestDto;
import com.university.cosmocats.dto.product.ProductResponseDto;
import com.university.cosmocats.dto.product.UpdateProductRequestDto;
import com.university.cosmocats.entity.CategoryEntity;
import com.university.cosmocats.entity.ProductEntity;
import com.university.cosmocats.exception.ProductNotFoundException;
import com.university.cosmocats.domain.product.Category;
import com.university.cosmocats.mapper.ProductMapper;
import com.university.cosmocats.repository.ProductRepository;
import com.university.cosmocats.service.CategoryService;
import com.university.cosmocats.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private ProductResponseDto testProduct;
    private Category testCategory;
    private ProductEntity testProductEntity;
    private CategoryEntity testCategoryEntity;
    private ProductRequestDto productRequestDto;


    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Spy
    private final ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .description(CATEGORY_DESCRIPTION)
                .build();

        testProduct = ProductResponseDto.builder()
                .id(VALID_PRODUCT_ID)
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .category(testCategory)
                .build();

        productRequestDto = ProductRequestDto.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .price(PRODUCT_PRICE)
                .categoryId(CATEGORY_ID)
                .build();

        testCategoryEntity = prepareCategoryEntity();

        testProductEntity = prepareProductEntity();
    }


    @Test
    @DisplayName("Should create product successfully")
    void testCreateProduct() {
        when(categoryService.findCategoryEntityById(CATEGORY_ID)).thenReturn(testCategoryEntity);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(testProductEntity);

        ProductResponseDto createdProduct = productService.createProduct(productRequestDto);

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
        when(productRepository.findById(VALID_PRODUCT_ID)).thenReturn(Optional.of(testProductEntity));

        ProductResponseDto product = productService.getProductById(VALID_PRODUCT_ID);

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
        when(productRepository.findById(NON_EXISTING_PRODUCT_ID)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProductById(NON_EXISTING_PRODUCT_ID)
        );

        assertEquals("Product with id: " + NON_EXISTING_PRODUCT_ID + " was not found", exception.getMessage());
        verify(productRepository).findById(NON_EXISTING_PRODUCT_ID);
    }

    @Test
    @DisplayName("Should get all products")
    void testGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);

        ProductEntity secondTestProductEntity = new ProductEntity();
        secondTestProductEntity.setId(2L);
        Page<ProductEntity> productResponseDtoPage = new PageImpl<>(List.of(testProductEntity, secondTestProductEntity));
        when(productRepository.findAll(pageable)).thenReturn(productResponseDtoPage);

        Page<ProductResponseDto> result = productService.getALlProducts(pageable);

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        verify(productRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should return empty list when no products exist")
    void testGetAllProductsWhenNoProductsExist() {
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<ProductResponseDto> result = productService.getALlProducts(pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(productRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Should delete product successfully when ID exists")
    void testDeleteProductWhenIdExists() {
        doNothing().when(productRepository).deleteById(VALID_PRODUCT_ID);

        productService.deleteProduct(VALID_PRODUCT_ID);

        verify(productRepository).deleteById(VALID_PRODUCT_ID);
    }

    @Test
    @DisplayName("Should update product successfully when ID exists")
    void testUpdateProductWhenIdExists() {
        // Given
        UpdateProductRequestDto updateRequest = UpdateProductRequestDto.builder()
                        .name("Updated Banana")
                        .description("Updated description")
                        .price(BigDecimal.valueOf(19.99))
                        .build();

        when(productRepository.findById(VALID_PRODUCT_ID)).thenReturn(Optional.of(testProductEntity));

        ProductResponseDto result = productService.updateProduct(VALID_PRODUCT_ID, updateRequest);

        assertNotNull(result);
        assertEquals(VALID_PRODUCT_ID, result.getId());
        assertEquals(result.getName(), "Updated Banana");
        verify(productRepository).findById(VALID_PRODUCT_ID);
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent product")
    void testUpdateNonExistentProduct() {
        UpdateProductRequestDto updateRequest = UpdateProductRequestDto.builder()
                .name("Updated Banana")
                .build();

        when(productRepository.findById(NON_EXISTING_PRODUCT_ID)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(
                ProductNotFoundException.class,
                () -> productService.updateProduct(NON_EXISTING_PRODUCT_ID, updateRequest)
        );

        assertEquals("Product with id: " + NON_EXISTING_PRODUCT_ID + " was not found", exception.getMessage());
        verify(productRepository).findById(NON_EXISTING_PRODUCT_ID);
    }

    private ProductEntity prepareProductEntity() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(PRODUCT_NAME);
        productEntity.setDescription(PRODUCT_DESCRIPTION);
        productEntity.setId(VALID_PRODUCT_ID);
        productEntity.setPrice(PRODUCT_PRICE);
        productEntity.setCategory(prepareCategoryEntity());

        return productEntity;
    }

    private CategoryEntity prepareCategoryEntity() {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(CATEGORY_ID);
        categoryEntity.setName(CATEGORY_NAME);
        categoryEntity.setDescription(CATEGORY_DESCRIPTION);

        return categoryEntity;
    }
}