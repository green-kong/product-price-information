package com.example.musinsaserver.brand.adaptor.in.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsaserver.brand.application.port.in.GetAllBrandsUseCase;
import com.example.musinsaserver.brand.application.port.in.RegisterBrandUseCase;
import com.example.musinsaserver.brand.application.port.in.dto.BrandRegisterRequest;
import com.example.musinsaserver.brand.application.port.in.dto.BrandResponse;

@RestController
@RequestMapping("api/brands")
public class BrandController implements BrandControllerDocs {
    private final RegisterBrandUseCase registerBrandUseCase;
    private final GetAllBrandsUseCase getAllBrandsUseCase;

    public BrandController(
            final RegisterBrandUseCase registerBrandUseCase,
            final GetAllBrandsUseCase getAllBrandsUseCase
    ) {
        this.registerBrandUseCase = registerBrandUseCase;
        this.getAllBrandsUseCase = getAllBrandsUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerBrand(@RequestBody final BrandRegisterRequest brandRegisterRequest) {
        final Long brandId = registerBrandUseCase.registerBrand(brandRegisterRequest);
        return ResponseEntity.created(URI.create("/api/brands/" + brandId)).build();
    }

    @GetMapping
    public ResponseEntity<List<BrandResponse>> getAllBrands() {
        final List<BrandResponse> brandResponses = getAllBrandsUseCase.getAllBrands();
        return ResponseEntity.ok(brandResponses);
    }
}
