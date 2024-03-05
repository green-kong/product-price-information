package com.example.musinsaserver.acceptance.fixture;

import static io.restassured.RestAssured.given;

import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;

import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CategoryAcceptanceFixture {
    public static Long registerCategory(final String category) {
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(new CategoryRegisterRequest(category))
                .when()
                .post("/api/categories")
                .then().log().all()
                .extract();

        final String[] locations = response.header("Location").split("/");
        return Long.parseLong(locations[locations.length - 1]);
    }
}
