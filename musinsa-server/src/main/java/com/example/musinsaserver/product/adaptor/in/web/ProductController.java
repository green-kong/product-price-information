package com.example.musinsaserver.product.adaptor.in.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsaserver.product.application.port.in.DeleteProductUseCase;
import com.example.musinsaserver.product.application.port.in.GetAllProductsUseCase;
import com.example.musinsaserver.product.application.port.in.RegisterProductUseCase;
import com.example.musinsaserver.product.application.port.in.UpdateProductUseCase;
import com.example.musinsaserver.product.application.port.in.dto.ProductPriceUpdateRequest;
import com.example.musinsaserver.product.application.port.in.dto.ProductResponse;
import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final RegisterProductUseCase registerProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;

    public ProductController(
            final RegisterProductUseCase registerProductUseCase,
            final UpdateProductUseCase updateProductUseCase,
            final DeleteProductUseCase deleteProductUseCase,
            final GetAllProductsUseCase getAllProductsUseCase
    ) {
        this.registerProductUseCase = registerProductUseCase;
        this.updateProductUseCase = updateProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.getAllProductsUseCase = getAllProductsUseCase;
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        final List<ProductResponse> allProducts = getAllProductsUseCase.getAllProducts();
        return ResponseEntity.ok(allProducts);
    }

    @PostMapping
    public ResponseEntity<Void> registerProduct(@RequestBody final RegisterProductRequest registerProductRequest) {
        final Long savedProductId = registerProductUseCase.registerProduct(registerProductRequest);
        return ResponseEntity.created(URI.create("/api/products/" + savedProductId)).build();
    }

    @PatchMapping("{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable final Long productId,
            @RequestBody final ProductPriceUpdateRequest productPriceUpdateRequest
    ) {
        updateProductUseCase.updateProductPrice(productId, productPriceUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable final Long productId) {
        deleteProductUseCase.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
