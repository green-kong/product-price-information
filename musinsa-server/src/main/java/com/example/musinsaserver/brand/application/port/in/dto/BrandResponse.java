package com.example.musinsaserver.brand.application.port.in.dto;

import com.example.musinsaserver.brand.domain.Brand;

public record BrandResponse(Long id, String name) {

    public static BrandResponse from(final Brand brand) {
        return new BrandResponse(brand.getId(), brand.getNameValue());
    }

}
