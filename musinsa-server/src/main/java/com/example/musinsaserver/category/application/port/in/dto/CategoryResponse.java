package com.example.musinsaserver.category.application.port.in.dto;

import com.example.musinsaserver.category.domain.Category;

public record CategoryResponse(Long id, String name) {

    public static CategoryResponse from(final Category category) {
        return new CategoryResponse(category.getId(), category.getNameValue());
    }

}
