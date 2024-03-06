package com.example.musinsaserver.brand.adaptor.in.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsaserver.brand.application.port.in.RegisterBrandUseCase;
import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;

@RestController
@RequestMapping("api/brands")
public class BrandController {
    private final RegisterBrandUseCase registerBrandUseCase;

    public BrandController(final RegisterBrandUseCase registerBrandUseCase) {
        this.registerBrandUseCase = registerBrandUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerBrand(@RequestBody final RegisterBrandRequest registerBrandRequest) {
        final Long brandId = registerBrandUseCase.registerBrand(registerBrandRequest);
        return ResponseEntity.created(URI.create("/api/brands/" + brandId)).build();
    }

    @GetMapping
    public ResponseEntity<Void> getAllBrands() {
        return null;
    }
}
