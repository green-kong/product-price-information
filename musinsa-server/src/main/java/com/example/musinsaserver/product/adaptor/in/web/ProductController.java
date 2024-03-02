package com.example.musinsaserver.product.adaptor.in.web;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsaserver.product.application.port.in.RegisterProductUseCase;
import com.example.musinsaserver.product.application.port.in.UpdateProductUseCase;
import com.example.musinsaserver.product.application.port.in.dto.ProductUpdateRequest;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final RegisterProductUseCase registerProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;

    public ProductController(
            final RegisterProductUseCase registerProductUseCase,
            final UpdateProductUseCase updateProductUseCase
    ) {
        this.registerProductUseCase = registerProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> registerProduct(@RequestBody final RegisterProductRequest registerProductRequest) {
        final Long savedProductId = registerProductUseCase.registerProduct(registerProductRequest);
        return ResponseEntity.created(URI.create("/api/product/" + savedProductId)).build();
    }

    @PatchMapping("{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable final Long productId,
            @RequestBody final ProductUpdateRequest productUpdateRequest
    ) {
        updateProductUseCase.updateProduct(productId, productUpdateRequest);
        return ResponseEntity.noContent().build();
    }
}
