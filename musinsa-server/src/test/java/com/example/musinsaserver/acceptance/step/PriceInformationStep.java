package com.example.musinsaserver.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.musinsaserver.acceptance.CucumberClient;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;

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
                .get("/api/price-informations/brands/{brandId}")
                .then().log().all()
                .extract();

        cucumberClient.setResponse(response);
    }
}
