package com.example.musinsaserver.brand.application.port.in.dto;

import com.example.musinsaserver.brand.domain.Brand;

public record RegisterBrandRequest(String name) {
    public Brand toBrand() {
        return Brand.createWithoutId(name);
    }
}
