package com.example.musinsaserver.product.acceptance;

import static com.example.musinsaserver.product.acceptance.fixture.ProductAcceptanceFixture.saveProductAndReturnSavedProductId;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import com.example.musinsaserver.product.application.port.in.dto.ProductUpdateRequest;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;
import com.example.musinsaserver.product.application.port.out.event.ProductDeleteEventPublisher;
import com.example.musinsaserver.product.application.port.out.event.ProductRegisterEventPublisher;
import com.example.musinsaserver.product.application.port.out.event.ProductUpdateEventPublisher;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductDeleteEvent;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductUpdateEvent;
import com.example.musinsaserver.product.application.port.out.validator.BrandValidator;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("프로덕트 인수테스트")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductAcceptanceTest {

    @LocalServerPort
    int port;

    @MockBean
    BrandValidator brandValidator;

    @MockBean
    ProductRegisterEventPublisher productRegisterEventPublisher;

    @MockBean
    ProductUpdateEventPublisher productUpdateEventPublisher;

    @MockBean
    ProductDeleteEventPublisher productDeleteEventPublisher;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("프로덕트 저장 인수테스트")
    class RegisterProduct {

        @Test
        @DisplayName("프로덕트가 정상적으로 저장한다.")
        void registerProduct() {
            //given
            when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
            doNothing().when(productRegisterEventPublisher).publishRegisterProductEvent(any(ProductRegisterEvent.class));
            final RegisterProductRequest requestBody = new RegisterProductRequest(20_000, "bag", 1L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/product")
                    .then().log().all()
                    .extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        }

        @Test
        @DisplayName("유효하지 않은 카테고리를 포함한 product를 저장하면 400을 반환한다.")
        void registerProductFailByInvalidCategory() {
            //given
            when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
            final RegisterProductRequest requestBody = new RegisterProductRequest(20_000, "invalidCategory", 1L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/product")
                    .then().log().all()
                    .extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("유효하지 않은 가격을 포함한 product를 저장하면 400을 반환한다.")
        void registerProductFailByInvalidPrice() {
            //given
            when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
            final RegisterProductRequest requestBody = new RegisterProductRequest(9, "bag", 1L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/product")
                    .then().log().all()
                    .extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("존재하지 않는 브랜드를 포함한 product를 저장하면 400을 반환한다.")
        void registerProductFailByInvalidBrand() {
            //given
            when(brandValidator.isExistedBrand(anyLong())).thenReturn(false);
            final RegisterProductRequest requestBody = new RegisterProductRequest(20_000, "bag", 0L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestBody)
                    .when()
                    .post("/api/product")
                    .then().log().all()
                    .extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("프로덕트 정보 수정 인수테스트")
    class update {

        @Test
        @DisplayName("저장된 product의 정보를 수정한다.")
        void updateProduct() {
            //given
            when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
            doNothing().when(productRegisterEventPublisher).publishRegisterProductEvent(any(ProductRegisterEvent.class));
            doNothing().when(productUpdateEventPublisher).publishUpdateProductEvent(any(ProductUpdateEvent.class));
            final Long savedProductId = saveProductAndReturnSavedProductId(10_000, "hat", 1L);

            final ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(20_000, "pants", 3L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .pathParam("productId", savedProductId)
                    .body(productUpdateRequest)
                    .when()
                    .patch("/api/product/{productId}")
                    .then().log().all()
                    .extract();

            //then
            verify(productUpdateEventPublisher, times(1)).publishUpdateProductEvent(any(ProductUpdateEvent.class));
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("존재하지 않는 프로덕트Id를 통해 수정하는 경우 400을 응답한다.")
        void updateFailByInvalidProductId() {
            //given
            when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
            doNothing().when(productRegisterEventPublisher).publishRegisterProductEvent(any(ProductRegisterEvent.class));
            final ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(20_000, "pants", 3L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .pathParam("productId", 0)
                    .body(productUpdateRequest)
                    .when()
                    .patch("/api/product/{productId}")
                    .then().log().all()
                    .extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("유효하지 않은 가격으로 수정하는 경우 400을 응답한다.")
        void updateFailByInvalidPrice() {
            //given
            when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
            doNothing().when(productRegisterEventPublisher).publishRegisterProductEvent(any(ProductRegisterEvent.class));
            final Long savedProductId = saveProductAndReturnSavedProductId(10_000, "hat", 1L);

            final ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(9, "pants", 3L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .pathParam("productId", savedProductId)
                    .body(productUpdateRequest)
                    .when()
                    .patch("/api/product/{productId}")
                    .then().log().all()
                    .extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("유효하지 않은 카테고리로 수정하는 경우 400을 응답한다.")
        void updateFailByInvalidCategory() {
            //given
            when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
            doNothing().when(productRegisterEventPublisher).publishRegisterProductEvent(any(ProductRegisterEvent.class));
            final Long savedProductId = saveProductAndReturnSavedProductId(10_000, "hat", 1L);

            final ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(20_000, "invalid", 3L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .pathParam("productId", savedProductId)
                    .body(productUpdateRequest)
                    .when()
                    .patch("/api/product/{productId}")
                    .then().log().all()
                    .extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        @DisplayName("존재하지 않는 brandId로 수정하는 경우 400을 응답한다.")
        void updateFailByInvalidBrandId() {
            //given
            when(brandValidator.isExistedBrand(1L)).thenReturn(true);
            when(brandValidator.isExistedBrand(3L)).thenReturn(false);
            doNothing().when(productRegisterEventPublisher).publishRegisterProductEvent(any(ProductRegisterEvent.class));
            final Long savedProductId = saveProductAndReturnSavedProductId(10_000, "hat", 1L);

            final ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(20_000, "accessories", 3L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .pathParam("productId", savedProductId)
                    .body(productUpdateRequest)
                    .when()
                    .patch("/api/product/{productId}")
                    .then().log().all()
                    .extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }

    @Nested
    @DisplayName("프로덕트 삭제 인수테스트")
    class delete {

        @Test
        @DisplayName("저장된 product의 정보를 삭제한다.")
        void deleteProduct() {
            //given
            when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
            doNothing().when(productRegisterEventPublisher).publishRegisterProductEvent(any(ProductRegisterEvent.class));
            doNothing().when(productDeleteEventPublisher).publishDeleteProductEvent(any(ProductDeleteEvent.class));
            final Long savedProductId = saveProductAndReturnSavedProductId(10_000, "hat", 1L);

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .pathParam("productId", savedProductId)
                    .when()
                    .delete("/api/product/{productId}")
                    .then().log().all()
                    .extract();

            //then
            verify(productDeleteEventPublisher, times(1)).publishDeleteProductEvent(any(ProductDeleteEvent.class));
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        @DisplayName("유효하지 않은 id를 통해 삭제를 시도하는 경우 400을 응답한다.")
        void deleteProductFail() {
            //given
            doNothing().when(productDeleteEventPublisher).publishDeleteProductEvent(any(ProductDeleteEvent.class));

            //when
            final ExtractableResponse<Response> response = given().log().all()
                    .contentType(ContentType.JSON)
                    .pathParam("productId", 0L)
                    .when()
                    .delete("/api/product/{productId}")
                    .then().log().all()
                    .extract();

            //then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }
}
