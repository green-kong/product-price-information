package com.example.musinsaserver.category.adaptor.in.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsaserver.category.application.port.in.CategoryRegisterUseCase;
import com.example.musinsaserver.category.application.port.in.GetAllCategoriesUseCase;
import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;
import com.example.musinsaserver.category.application.port.in.dto.CategoryResponse;

@RequestMapping("api/categories")
@RestController
public class CategoryController {

    private final CategoryRegisterUseCase categoryRegisterUseCase;
    private final GetAllCategoriesUseCase getAllCategoriesUseCase;

    public CategoryController(
            final CategoryRegisterUseCase categoryRegisterUseCase,
            final GetAllCategoriesUseCase getAllCategoriesUseCase
    ) {
        this.categoryRegisterUseCase = categoryRegisterUseCase;
        this.getAllCategoriesUseCase = getAllCategoriesUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerCategory(@RequestBody final CategoryRegisterRequest categoryRegisterRequest) {
        final Long categoryId = categoryRegisterUseCase.save(categoryRegisterRequest);
        return ResponseEntity.created(URI.create(String.valueOf(categoryId))).build();
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        final List<CategoryResponse> categories = getAllCategoriesUseCase.getAllCategories();
        return ResponseEntity.ok(categories);
    }
}
