package com.example.musinsaserver.priceinformation.adaptor.in.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.musinsaserver.common.dto.ErrorResponse;
import com.example.musinsaserver.priceinformation.application.port.in.dto.HighestAndLowestPriceInformationByCategoryResponse;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByCategoryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Price Information Controller", description = "Price Information API")
public interface PriceInformationControllerDocs {

    @Operation(summary = "특정 브랜드의 카테고리별 최저 가격 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "특정 브랜드의 카테고리별 최저 가격 정보를 조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = LowestPriceInformationByBrandResponses.class))
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "가격정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "가격정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<LowestPriceInformationByBrandResponses> getLowestPriceProductInformationByBrand(
            @PathVariable("brandId")
            @Parameter(description = "카테고리별 최저 가격 정보를 조회할 브랜드의 id") final Long brandId
    );

    @Operation(summary = "전체 상품 중 카테고리 별 가장 저렴한 가격 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "전체 상품 중 카테고리 별 가장 저렴한 가격 정보를 조회한다.",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LowestPriceInformationByCategoryResponse.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "가격정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "가격정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<LowestPriceInformationByCategoryResponse> getLowestPriceProductInformationByCategory();

    @Operation(summary = "특정 카테고리의 가장 비싼 상품의 가격 정보와 가장 저렴한 상품의 가격 정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "특정 카테고리의 가장 비싼 상품의 가격 정보와 가장 저렴한 상품의 가격 정보를 조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = LowestPriceInformationByBrandResponses.class)
                    )),
            @ApiResponse(
                    responseCode = "400",
                    description = "가격정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "가격정보 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<HighestAndLowestPriceInformationByCategoryResponse> getHighestAndLowestInformationByCategory(
            @PathVariable("categoryName")
            @Parameter(description = "가격 정보를 조회할 카테고리의 이름")
            final String category
    );
}
