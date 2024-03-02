package com.example.musinsaserver.product.adaptor.in.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsaserver.product.application.port.in.RegisterProductUseCase;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final RegisterProductUseCase registerProductUseCase;

    public ProductController(final RegisterProductUseCase registerProductUseCase) {
        this.registerProductUseCase = registerProductUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerProduct(@RequestBody final RegisterProductRequest registerProductRequest) {
        registerProductUseCase.registerProduct(registerProductRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
