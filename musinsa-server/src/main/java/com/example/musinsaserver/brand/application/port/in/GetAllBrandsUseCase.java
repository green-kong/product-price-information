package com.example.musinsaserver.brand.application.port.in;

import java.util.List;

import com.example.musinsaserver.brand.application.port.in.dto.BrandResponse;

public interface GetAllBrandsUseCase {
    List<BrandResponse> getAllBrands();
}
