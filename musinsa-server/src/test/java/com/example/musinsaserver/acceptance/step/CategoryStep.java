package com.example.musinsaserver.acceptance.step;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.example.musinsaserver.acceptance.CucumberClient;
import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;
import com.example.musinsaserver.category.application.port.in.dto.CategoryResponse;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

    @When("저장된 모든 카테고리를 조회한다.")
    public void getAllCategories() {
        final ExtractableResponse<Response> response = given().log().all()
                .when()
                .get("/api/categories")
                .then().log().all()
                .extract();
        cucumberClient.setResponse(response);
    }

    @Then("응답은 아래의 카테고리 이름을 포함하고 있다.")
    public void checkGetAllCategoriesResponse(DataTable dataTable) {
        final List<String> expectedCategoryNames = dataTable.asList(String.class);
        final List<Long> expectedCategoryIds = expectedCategoryNames.stream()
                .map(cucumberClient::getData)
                .toList();

        final ExtractableResponse<Response> response = cucumberClient.getResponse();
        final CategoryResponse[] parsedResponse = response.as(CategoryResponse[].class);

        final List<Long> resultIds = Arrays.stream(parsedResponse)
                .map(CategoryResponse::id)
                .toList();
        final List<String> resultNames = Arrays.stream(parsedResponse)
                .map(CategoryResponse::name)
                .toList();

        assertSoftly(softAssertions -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(resultIds).containsExactlyInAnyOrderElementsOf(expectedCategoryIds);
            assertThat(resultNames).containsExactlyInAnyOrderElementsOf(expectedCategoryNames);
        });
    }
}
