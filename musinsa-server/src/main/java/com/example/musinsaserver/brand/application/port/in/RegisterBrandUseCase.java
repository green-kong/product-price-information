package com.example.musinsaserver.brand.application.port.in;

import com.example.musinsaserver.brand.application.port.in.dto.BrandRegisterRequest;

public interface RegisterBrandUseCase {
    Long registerBrand(final BrandRegisterRequest brandRegisterRequest);
}
