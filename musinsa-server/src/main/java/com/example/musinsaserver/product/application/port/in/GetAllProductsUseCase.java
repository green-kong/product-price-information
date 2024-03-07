package com.example.musinsaserver.product.application.port.in;

import java.util.List;

import com.example.musinsaserver.product.application.port.in.dto.ProductResponse;

public interface GetAllProductsUseCase {
    List<ProductResponse> getAllProducts();

}
