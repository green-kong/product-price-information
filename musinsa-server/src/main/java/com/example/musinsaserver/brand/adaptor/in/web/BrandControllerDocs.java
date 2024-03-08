package com.example.musinsaserver.brand.adaptor.in.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.example.musinsaserver.brand.application.port.in.dto.BrandRegisterRequest;
import com.example.musinsaserver.brand.application.port.in.dto.BrandResponse;
import com.example.musinsaserver.common.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Brand Controller", description = "Brand API")
public interface BrandControllerDocs {

    @Operation(summary = "브랜드 등록", description = "새로운 브랜드를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "브랜드 등록 성공",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE)),
            @ApiResponse(
                    responseCode = "400",
                    description = "브랜드 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "브랜드 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<Void> registerBrand(final BrandRegisterRequest brandRegisterRequest);


    @Operation(summary = "브랜드 목록 조회", description = "저장된 모든 브랜드를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "전체 브랜드 목록 조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = BrandResponse.class))
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "브랜드 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<List<BrandResponse>> getAllBrands();
}
