package com.example.musinsaserver.category.application.port.in;

import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;

public interface CategoryRegisterUseCase {
    Long save(CategoryRegisterRequest categoryRegisterRequest);
}
