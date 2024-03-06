package com.example.musinsaserver.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.musinsaserver.acceptance.CucumberClient;
import com.example.musinsaserver.priceinformation.application.port.in.dto.HighestAndLowestPriceInformationByCategoryResponse;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByCategoryResponse;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class PriceInformationStep {
    @Autowired
    CucumberClient cucumberClient;

    @Then("{string}의 저렴한 상품리스트는 {int}개이고, 총 가격의 합은 {int}원이다.")
    public void checkResponse(String brand, int expectedCount, int expectedSum) {
        final ExtractableResponse<Response> response = cucumberClient.getResponse();

        final var parsedResponse = response.as(LowestPriceInformationByBrandResponses.class);
        assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(parsedResponse.sum()).isEqualTo(expectedSum);
            assertThat(parsedResponse.brandName()).isEqualTo(brand);
            assertThat(parsedResponse.lowestPriceInformationResponses()).hasSize(expectedCount);
        });
    }

    @When("{string}의 카테고리 별 가장 저렴한 상품들의 정보를 반환한다.")
    public void getLowestPriceProductInformationByBrand(final String brand) {
        final Long brandId = cucumberClient.getData(brand);
        final ExtractableResponse<Response> response = given().log().all()
                .pathParam("brandId", brandId)
                .when()
                .get("/api/price-informations/brands/{brandId}/lowest")
                .then().log().all()
                .extract();

        cucumberClient.setResponse(response);
    }

    @When("전체 상품 중 카테고리별 가장 저렴한 상품 정보를 반환한다.")
    public void lowestPriceProductByCategory() {
        final ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/api/price-informations/categories/lowest")
                .then().log().all()
                .extract();

        cucumberClient.setResponse(response);
    }

    @Then("반환된 상품 정보의 개수는 {int}이고, 가격의 총합은 {int}원이다.")
    public void checkLowestPriceProductByCategoryResponse(int expectedCount, int expectedSum) {
        final ExtractableResponse<Response> response = cucumberClient.getResponse();

        final var parsedResponse = response.as(LowestPriceInformationByCategoryResponse.class);
        assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(parsedResponse.sum()).isEqualTo(expectedSum);
            assertThat(parsedResponse.lowestPriceInformationResponses()).hasSize(expectedCount);
        });
    }

    @When("{string} 카테고리의 최소가격정보와 최대가격정보를 반환한다.")
    public void searchHighestAndLowest(String category) {
        final ExtractableResponse<Response> response = given().log().all()
                .pathParam("categoryName", category)
                .when()
                .get("/api/price-informations/categories/{categoryName}/highest-lowest")
                .then().log().all()
                .extract();

        cucumberClient.setResponse(response);
    }

    @Then("카테고리는 {string}, 최소 가격정보의 브랜드는 {string} 가격은 {int}원이고, 최대 가격정보의 브랜드는 {string} 가격은 {int}원이다.")
    public void checkHighestLowest(String category, String lowBrand, int lowPrice, String highBrand, int highPrice) {
        final ExtractableResponse<Response> response = cucumberClient.getResponse();

        final var parsedResponse = response.as(HighestAndLowestPriceInformationByCategoryResponse.class);

        assertSoftly(softAssertions -> {
            assertThat(parsedResponse.category()).isEqualTo(category);
            assertThat(parsedResponse.highest().brand()).isEqualTo(highBrand);
            assertThat(parsedResponse.highest().price()).isEqualTo(highPrice);
            assertThat(parsedResponse.lowest().brand()).isEqualTo(lowBrand);
            assertThat(parsedResponse.lowest().price()).isEqualTo(lowPrice);
        });
    }
}
