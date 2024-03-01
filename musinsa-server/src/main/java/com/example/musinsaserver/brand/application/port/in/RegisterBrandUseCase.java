package com.example.musinsaserver.brand.application.port.in;

import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;

public interface RegisterBrandUseCase {
    Long registerBrand(final RegisterBrandRequest registerBrandRequest);
}
