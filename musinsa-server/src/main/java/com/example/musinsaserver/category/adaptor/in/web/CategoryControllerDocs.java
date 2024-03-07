package com.example.musinsaserver.category.adaptor.in.web;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;
import com.example.musinsaserver.category.application.port.in.dto.CategoryResponse;
import com.example.musinsaserver.common.dto.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Category Controller", description = "Category API")
public interface CategoryControllerDocs {

    @Operation(summary = "카테고리 등록", description = "새로운 카테고리를 등록한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "카테고리 등록 성공",
                    content = @Content(mediaType = APPLICATION_JSON_VALUE)),
            @ApiResponse(
                    responseCode = "400",
                    description = "카테고리 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "카테고리 등록 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<Void> registerCategory(@RequestBody final CategoryRegisterRequest categoryRegisterRequest);

    @Operation(summary = "카테고리 목록 조회", description = "저장된 모든 카테고리를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "전체 카테고리 목록 조회 성공",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = CategoryResponse.class))
                    )),
            @ApiResponse(
                    responseCode = "500",
                    description = "카테고리 조회 실패",
                    content = @Content(
                            mediaType = APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    ResponseEntity<List<CategoryResponse>> getAllCategories();
}
