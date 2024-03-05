package com.example.musinsaserver.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.acceptance.CucumberClient;
import com.example.musinsaserver.product.application.port.in.dto.ProductPriceUpdateRequest;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ProductStep {

    @Autowired
    CucumberClient cucumberClient;

    @When("{string} 브랜드의 {string} 카테고리에 {int}원 짜리 프로덕트를 생성한다.")
    public void createCategory(final String brandName, final String categoryName, final int price) {
        final Long categoryId = cucumberClient.getData(categoryName);
        final Long brandId = cucumberClient.getData(brandName);
        final RegisterProductRequest requestBody = new RegisterProductRequest(price, categoryId, brandId);
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/api/products")
                .then().log().all()
                .extract();
        final String[] locations = response.header("Location").split("/");
        final long productId = Long.parseLong(locations[locations.length - 1]);
        cucumberClient.addData("product" + productId, productId);
        cucumberClient.setResponse(response);
    }

    @When("{string} 의 가격 정보를 {int}로 수정한다.")
    public void updatePrice(final String product, final int price) {
        final Long productId = cucumberClient.getData(product);
        final ProductPriceUpdateRequest request = new ProductPriceUpdateRequest(price);
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("productId", productId)
                .body(request)
                .when()
                .patch("/api/products/{productId}")
                .then().log().all()
                .extract();
        cucumberClient.setResponse(response);
    }

    @When("{string}을 삭제한다.")
    public void delete(String product) {
        final Long productId = cucumberClient.getData(product);
        final ExtractableResponse<Response> response = given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("productId", productId)
                .when()
                .delete("/api/products/{productId}")
                .then().log().all()
                .extract();
        cucumberClient.setResponse(response);
    }

    @Then("{int}을 응답한다.")
    public void checkResponse(final int expectedStatusCode) {
        final ExtractableResponse<Response> response = cucumberClient.getResponse();
        assertThat(response.statusCode()).isEqualTo(expectedStatusCode);
    }
}
