package com.example.musinsaserver.category.application.port.in.dto;

import com.example.musinsaserver.category.domain.Category;

import io.swagger.v3.oas.annotations.media.Schema;

public record CategoryRegisterRequest(@Schema(description = "카테고리 이름", minLength = 2) String name) {
    public Category toCategory() {
        return Category.createWithoutId(name);
    }
}
