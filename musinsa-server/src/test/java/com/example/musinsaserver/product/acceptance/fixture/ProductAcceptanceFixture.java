package com.example.musinsaserver.product.acceptance.fixture;

import static io.restassured.RestAssured.given;

import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class ProductAcceptanceFixture {
    public static Long saveProductAndReturnSavedProductId(final int price, final Long categoryId, final long brandId) {
        final RegisterProductRequest requestBody = new RegisterProductRequest(price, categoryId, brandId);
        final Response response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/product");
        final String[] splitLocation = response.header("Location").split("/");
        return Long.parseLong(splitLocation[splitLocation.length - 1]);
    }
}
