package com.example.musinsaserver.category.adaptor.in.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.common.validator.CategoryValidator;

@Component
public class RepositoryCategoryValidator implements CategoryValidator {

    private final CategoryRepository categoryRepository;

    public RepositoryCategoryValidator(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public boolean isExistedCategory(final Long categoryId) {
        final Optional<Category> category = categoryRepository.findById(categoryId);
        return category.isPresent();
    }
}
