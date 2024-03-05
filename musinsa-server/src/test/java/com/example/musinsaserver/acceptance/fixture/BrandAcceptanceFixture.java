package com.example.musinsaserver.acceptance.fixture;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class BrandAcceptanceFixture {
    public static Long saveBrand(final String name) {
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(JSON)
                .body(new RegisterBrandRequest(name))
                .when()
                .post("/api/brand")
                .then().log().all()
                .extract();

        final String[] locations = response.header("Location").split("/");
        return Long.parseLong(locations[locations.length - 1]);
    }
}
