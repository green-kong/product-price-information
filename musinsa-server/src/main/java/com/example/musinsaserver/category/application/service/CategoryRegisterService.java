package com.example.musinsaserver.category.application.service;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.category.application.port.in.CategoryRegisterUseCase;
import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;
import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;

@Service
public class CategoryRegisterService implements CategoryRegisterUseCase {

    private final CategoryRepository categoryRepository;

    public CategoryRegisterService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Long save(final CategoryRegisterRequest categoryRegisterRequest) {
        final Category category = categoryRegisterRequest.toCategory();
        final Category saved = categoryRepository.save(category);
        return saved.getId();
    }
}
