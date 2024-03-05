package com.example.musinsaserver.acceptance.step;

import static io.restassured.RestAssured.given;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.acceptance.CucumberClient;
import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;

import io.cucumber.java.en.Given;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class CategoryStep {

    @Autowired
    CucumberClient cucumberClient;

    @Given("{string} 카테고리를 생성한다.")
    public void createCategory(final String category) {
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(new CategoryRegisterRequest(category))
                .when()
                .post("/api/categories")
                .then().log().all()
                .extract();

        final String[] locations = response.header("Location").split("/");
        final long categoryId = Long.parseLong(locations[locations.length - 1]);
        cucumberClient.addData(category, categoryId);
    }

}
