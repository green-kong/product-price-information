package com.example.musinsaserver.product.adaptor.in.web;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.musinsaserver.common.dto.ErrorResponse;
import com.example.musinsaserver.product.application.port.in.dto.ProductPriceUpdateRequest;
import com.example.musinsaserver.product.application.port.in.dto.ProductResponse;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Product Controller", description = "Product API")
interface ProductControllerDocs {

    @Operation(summary = "프로덕트 목록 조회", description = "저장된 모든 프로덕트를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "전체 프로덕트 목록 조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = ProductResponse.class))
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "프로덕트 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<List<ProductResponse>> getAllProducts();

    @Operation(summary = "프로덕트 등록", description = "새로운 프로덕트를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "프로덕트 등록 성공",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE)),
            @ApiResponse(
                    responseCode = "400",
                    description = "프로덕트 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "프로덕트 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<Void> registerProduct(final RegisterProductRequest registerProductRequest);

    @Operation(summary = "프로덕트의 가격을 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "프로덕트 가격 수정 성공",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE)),
            @ApiResponse(
                    responseCode = "400",
                    description = "프로덕트 가격 수정 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "프로덕트 가격 수정 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<Void> updateProduct(
            @PathVariable
            @Parameter(description = "가격을 수정할 프로덕트의 id") final Long productId,
            ProductPriceUpdateRequest productPriceUpdateRequest
    );

    @Operation(summary = "프로덕트의 삭제한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "프로덕트 삭제 성공",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE)),
            @ApiResponse(
                    responseCode = "400",
                    description = "프로덕트 삭제 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "프로덕트 삭제 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<Void> deleteProduct(
            @PathVariable
            @Parameter(description = "삭제할 프로덕트의 id") final Long productId
    );
}
