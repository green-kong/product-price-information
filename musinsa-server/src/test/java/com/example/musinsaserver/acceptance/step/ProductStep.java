package com.example.musinsaserver.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.musinsaserver.acceptance.CucumberClient;
import com.example.musinsaserver.product.application.port.in.dto.ProductPriceUpdateRequest;
import com.example.musinsaserver.product.application.port.in.dto.ProductResponse;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;

import io.cucumber.datatable.DataTable;
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

    @When("모든 프로덕트를 조회한다.")
    public void findAllProducts() {
        final ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/api/products")
                .then().log().all()
                .extract();
        cucumberClient.setResponse(response);
    }

    @Then("응답은 아래 목록의 프로덕트를 모두 포함한다.")
    public void checkFindAllProductsResponse(DataTable dataTable) {
        final List<List<String>> lists = dataTable.asLists(String.class);
        final List<ProductResponse> expected = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            final Long productId = cucumberClient.getData("product" + (i + 1));
            final List<String> datas = lists.get(i);
            final String brand = datas.get(0);
            final String category = datas.get(1);
            final int price = Integer.parseInt(datas.get(2));
            expected.add(new ProductResponse(productId, price, brand, category));
        }

        final ExtractableResponse<Response> response = cucumberClient.getResponse();
        final ProductResponse[] parsedResponse = response.as(ProductResponse[].class);
        assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(parsedResponse).containsExactlyInAnyOrderElementsOf(expected);
        });
    }
}
