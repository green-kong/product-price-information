package com.example.musinsaserver.brand.adaptor.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsaserver.brand.application.port.in.RegisterBrandUseCase;
import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;

@RestController
@RequestMapping("api/brand")
public class BrandController {
    private final RegisterBrandUseCase registerBrandUseCase;

    public BrandController(final RegisterBrandUseCase registerBrandUseCase) {
        this.registerBrandUseCase = registerBrandUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerBrand(@RequestBody final RegisterBrandRequest registerBrandRequest) {
        registerBrandUseCase.registerBrand(registerBrandRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
