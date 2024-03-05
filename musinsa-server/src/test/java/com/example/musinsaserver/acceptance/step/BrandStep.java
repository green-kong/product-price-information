package com.example.musinsaserver.acceptance.step;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.acceptance.CucumberClient;
import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;

import io.cucumber.java.en.Given;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class BrandStep {

    @Autowired
    CucumberClient cucumberClient;

    @Given("{string} 브랜드를 생성한다.")
    public void createBrand(final String brand) {
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(JSON)
                .body(new RegisterBrandRequest(brand))
                .when()
                .post("/api/brands")
                .then().log().all()
                .extract();

        final String[] locations = response.header("Location").split("/");
        final long savedBrandId = Long.parseLong(locations[locations.length - 1]);
        cucumberClient.addData(brand, savedBrandId);
    }
}
