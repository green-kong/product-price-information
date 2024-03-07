package com.example.musinsaserver.brand.application.port.in.dto;

import com.example.musinsaserver.brand.domain.Brand;

import io.swagger.v3.oas.annotations.media.Schema;

public record BrandRegisterRequest(@Schema(description = "브랜드 이름", minLength = 2) String name) {
    public Brand toBrand() {
        return Brand.createWithoutId(name);
    }
}
