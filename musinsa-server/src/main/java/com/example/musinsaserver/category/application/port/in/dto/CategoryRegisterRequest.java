package com.example.musinsaserver.category.application.port.in.dto;

import com.example.musinsaserver.category.domain.Category;

public record CategoryRegisterRequest(String category) {
    public Category toCategory() {
        return Category.createWithoutId(category);
    }
}
