package com.example.musinsaserver.category.adaptor.in.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsaserver.category.application.port.in.CategoryRegisterUseCase;
import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;

@RequestMapping("api/categories")
@RestController
public class CategoryController {

    private final CategoryRegisterUseCase categoryRegisterUseCase;

    public CategoryController(final CategoryRegisterUseCase categoryRegisterUseCase) {
        this.categoryRegisterUseCase = categoryRegisterUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerCategory(@RequestBody final CategoryRegisterRequest categoryRegisterRequest) {
        final Long categoryId = categoryRegisterUseCase.save(categoryRegisterRequest);
        return ResponseEntity.created(URI.create(String.valueOf(categoryId))).build();
    }
}
