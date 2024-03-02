package com.example.musinsaserver.product.acceptance;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;
import com.example.musinsaserver.product.application.port.out.validator.BrandValidator;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductAcceptanceTest {

    @LocalServerPort
    int port;

    @MockBean
    BrandValidator brandValidator;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("프로덕트가 정상적으로 저장되면 201을 반환한다.")
    void registerProduct() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
        final RegisterProductRequest requestBody = new RegisterProductRequest(20_000, "bag", 1L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/product")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("유효하지 않은 카테고리를 포함한 product를 저장하면 400을 반환한다.")
    void registerProductFailByInvalidCategory() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
        final RegisterProductRequest requestBody = new RegisterProductRequest(20_000, "invalidCategory", 1L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/product")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("유효하지 않은 가격을 포함한 product를 저장하면 400을 반환한다.")
    void registerProductFailByInvalidPrice() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
        final RegisterProductRequest requestBody = new RegisterProductRequest(9, "bag", 1L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/product")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("존재하지 않는 브랜드를 포함한 product를 저장하면 400을 반환한다.")
    void registerProductFailByInvalidBrand() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(false);
        final RegisterProductRequest requestBody = new RegisterProductRequest(20_000, "bag", 0L);

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/product")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
