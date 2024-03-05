package com.example.musinsaserver.acceptance;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;
import com.example.musinsaserver.support.BaseTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CategoryAcceptanceTest extends BaseTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("카테고리를 저장한다.")
    void createCategory() {
        //given
        final String name = "바지";

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(new CategoryRegisterRequest(name))
                .when()
                .post("/api/categories")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("유효하지 않은 이름으로 카테고리를 생성하면 400을 응답한다.")
    void createCategoryWithInvalidCategory() {
        //given
        final String invalidCategory = "바";

        //when
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(new CategoryRegisterRequest(invalidCategory))
                .when()
                .post("/api/categories")
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
