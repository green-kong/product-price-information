package com.example.musinsaserver.category.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.category.application.port.in.CategoryRegisterUseCase;
import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;
import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.category.exception.DuplicatedCategoryNameException;

@Service
@Transactional
public class CategoryRegisterService implements CategoryRegisterUseCase {

    private final CategoryRepository categoryRepository;

    public CategoryRegisterService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Long save(final CategoryRegisterRequest categoryRegisterRequest) {
        final Category category = categoryRegisterRequest.toCategory();
        validate(category);
        final Category saved = categoryRepository.save(category);
        return saved.getId();
    }

    private void validate(final Category category) {
        categoryRepository.findByName(category.getNameValue())
                .ifPresent(found -> {
                    throw new DuplicatedCategoryNameException(category.getNameValue());
                });
    }
}
