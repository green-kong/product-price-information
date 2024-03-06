package com.example.musinsaserver.acceptance;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;
import com.example.musinsaserver.common.dto.ErrorResponse;
import com.example.musinsaserver.support.BaseTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class BrandAcceptanceTest extends BaseTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("브랜드를 저장한다.")
    void registerBrand() {
        //given
        final RegisterBrandRequest registerBrandRequest = new RegisterBrandRequest("brandA");

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(JSON)
                .body(registerBrandRequest)
                .when()
                .post("/api/brands")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(CREATED.value());
    }

    @Test
    @DisplayName("유효하지 않은 브랜드명으로 브랜드를 생성할 시 400을 응답한다.")
    void registerBrandWithInvalidName() {
        //given
        final RegisterBrandRequest registerBrandRequest = new RegisterBrandRequest("  ");

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(JSON)
                .body(registerBrandRequest)
                .when()
                .post("/api/brands")
                .then().log().all()
                .extract();

        //then
        final ErrorResponse errorResponse = response.body().as(ErrorResponse.class);
        assertThat(response.statusCode()).isEqualTo(BAD_REQUEST.value());
        assertThat(errorResponse.code()).isEqualTo("BRAND0001");
        assertThat(errorResponse.message()).contains("브랜드의 이름은 한글자 이상이어야 합니다.");
    }
}
