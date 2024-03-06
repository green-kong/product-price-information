package com.example.musinsaserver.acceptance.step;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.musinsaserver.acceptance.CucumberClient;
import com.example.musinsaserver.brand.application.port.in.dto.BrandResponse;
import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

    @When("모든 브랜드를 조회한다.")
    public void getAllBrands() {
        final ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/api/brands")
                .then().log().all()
                .extract();
        cucumberClient.setResponse(response);
    }

    @Then("응답은 다음의 정보를 포함하고 있다.")
    public void checkAllBrandsResponse(DataTable dataTable) {
        final List<String> expectedBrandNames = dataTable.asList(String.class);
        final List<Long> expectedBrandIds = expectedBrandNames.stream()
                .map(cucumberClient::getData)
                .toList();

        final ExtractableResponse<Response> response = cucumberClient.getResponse();
        final BrandResponse[] parsedResponse = response.as(BrandResponse[].class);
        final List<String> brandNameResponses = Arrays.stream(parsedResponse)
                .map(BrandResponse::name)
                .toList();
        final List<Long> brandIdResponses = Arrays.stream(parsedResponse)
                .map(BrandResponse::id)
                .toList();

        assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(brandNameResponses).containsExactlyInAnyOrderElementsOf(expectedBrandNames);
            assertThat(brandIdResponses).containsExactlyInAnyOrderElementsOf(expectedBrandIds);
        });
    }
}
