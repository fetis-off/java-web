package com.university.cosmocats.controller;

import com.university.cosmocats.dto.product.CreateProductRequestDto;
import com.university.cosmocats.util.ProblemDetailFieldNames;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIT {

    private static final String CATEGORY_NAME = "Food";

    private static final Long VALID_PRODUCT_ID = 1L;
    private static final Long SECOND_VALID_PRODUCT_ID = 2L;
    private static final Long INVALID_PRODUCT_ID = 100L;
    private static final String PRODUCT_NAME = "Banana";
    private static final String INVALID_PRODUCT_NAME = "R";
    private static final String PRODUCT_DESCRIPTION = "Banana is a yellow fruit";
    private static final BigDecimal PRODUCT_PRICE = BigDecimal.valueOf(15.99);
    private static final BigDecimal INVALID_PRODUCT_PRICE = BigDecimal.valueOf(-0.99);

    private static final String PRODUCT_ID_FIELD = "id";
    private static final String PRODUCT_NAME_FIELD = "name";
    private static final String PRODUCT_DESCRIPTION_FIELD = "description";
    private static final String PRODUCT_PRICE_FIELD = "price";

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("Should create product and return 201 CREATED")
    void createProduct_validRequest_shouldReturnCreatedProduct() {
        CreateProductRequestDto productRequestDto = CreateProductRequestDto.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .category(CATEGORY_NAME)
                .price(PRODUCT_PRICE)
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(productRequestDto)
                .when()
                .post("api/v1/products")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body(PRODUCT_NAME_FIELD, equalTo(productRequestDto.getName()))
                .body(PRODUCT_DESCRIPTION_FIELD, equalTo(productRequestDto.getDescription()))
                .body(PRODUCT_PRICE_FIELD, equalTo(productRequestDto.getPrice().floatValue()));
    }

    @Test
    @DisplayName("Should not create product with invalid name and return 400 Bad request")
    void createProduct_invalidName_shouldReturnBadRequest() {
        CreateProductRequestDto invalidRequestDto = CreateProductRequestDto.builder()
                .name(INVALID_PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .category(CATEGORY_NAME)
                .price(PRODUCT_PRICE)
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(invalidRequestDto)
                .when()
                .post("api/v1/products")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_STATUS, equalTo(HttpStatus.BAD_REQUEST.value()))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_TITLE_FIELD, equalTo("Field Validation Exception"))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_FIELD_NAME, equalTo(PRODUCT_NAME_FIELD))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_REASON_FIELD, equalTo("Name should be between 3 and 100 characters"));
    }

    @Test
    @DisplayName("Should not create product with invalid price and return 400 Bad request")
    void createProduct_invalidPrice_shouldReturnBadRequest() {
        CreateProductRequestDto invalidRequestDto = CreateProductRequestDto.builder()
                .name(PRODUCT_NAME)
                .description(PRODUCT_DESCRIPTION)
                .category(CATEGORY_NAME)
                .price(INVALID_PRODUCT_PRICE)
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(invalidRequestDto)
                .when()
                .post("api/v1/products")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_STATUS, equalTo(HttpStatus.BAD_REQUEST.value()))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_TITLE_FIELD, equalTo("Field Validation Exception"))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_FIELD_NAME, equalTo(PRODUCT_PRICE_FIELD))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_REASON_FIELD, equalTo("Price should be greater than 0"));
    }

    @Test
    @DisplayName("Should return list of all products")
    void getAllProducts_shouldReturnProductList() {
        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("api/v1/products")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("$", notNullValue())
                .body("size()", equalTo(2)); //because mocked database contains 2 products
    }

    @Test
    @DisplayName("Should return product by valid id and status 200 OK")
    void getProductById_validId_shouldReturnProduct() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("api/v1/products/{id}", VALID_PRODUCT_ID)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(PRODUCT_ID_FIELD, equalTo(VALID_PRODUCT_ID.intValue()))
                .body(PRODUCT_NAME_FIELD, equalTo("iPhone 15"))
                .body(PRODUCT_DESCRIPTION_FIELD, equalTo("Latest Apple smartphone with A17 chip"))
                .body(PRODUCT_PRICE_FIELD, equalTo(BigDecimal.valueOf(999.99).floatValue()));
    }

    @Test
    @DisplayName("Should return 404 while getting product by id")
    void getProductById_invalidId_shouldReturnProduct() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .get("api/v1/products/{id}", INVALID_PRODUCT_ID)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_FIELD, equalTo("Product with ID: 100 not found"))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_TITLE_FIELD, equalTo("Product Not Found"))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_STATUS, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @DisplayName("Should update existing product and return 200 OK")
    void updateProduct_validRequest_shouldReturnUpdatedProduct() {
        CreateProductRequestDto updateRequest = CreateProductRequestDto.builder()
                .name("Updated Name")
                .description("Updated description")
                .category("Updated Category")
                .price(BigDecimal.valueOf(1099.99))
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("/api/v1/products/{id}", SECOND_VALID_PRODUCT_ID)
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(PRODUCT_ID_FIELD, equalTo(SECOND_VALID_PRODUCT_ID.intValue()))
                .body(PRODUCT_NAME_FIELD, equalTo(updateRequest.getName()))
                .body(PRODUCT_DESCRIPTION_FIELD, equalTo(updateRequest.getDescription()))
                .body(PRODUCT_PRICE_FIELD, equalTo(updateRequest.getPrice().floatValue()));
    }

    @Test
    @DisplayName("Should return 404 while updating product by id")
    void updateProduct_invalidId_shouldReturnNotFound() {
        CreateProductRequestDto updateRequest = CreateProductRequestDto.builder()
                .name("Updated Name")
                .description("Updated description")
                .category("Updated Category")
                .price(BigDecimal.valueOf(1099.99))
                .build();

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when()
                .put("api/v1/products/{id}", INVALID_PRODUCT_ID)
                .then()
                .log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_FIELD, equalTo("Product with ID: 100 not found"))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_TITLE_FIELD, equalTo("Product Not Found"))
                .body(ProblemDetailFieldNames.PROBLEM_DETAIL_STATUS, equalTo(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    @DisplayName("Should delete product by valid id")
    void deleteProduct_validId_shouldReturnNotFound() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete("api/v1/products/{id}", VALID_PRODUCT_ID)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

        given()
                .port(port)
                .when()
                .get("/api/v1/products/{id}", VALID_PRODUCT_ID)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Should return 204 while deleting product by non existing id")
    void deleteProduct_invalidId_shouldReturnNotFound() {

        given()
                .port(port)
                .contentType(ContentType.JSON)
                .when()
                .delete("api/v1/products/{id}", INVALID_PRODUCT_ID)
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}