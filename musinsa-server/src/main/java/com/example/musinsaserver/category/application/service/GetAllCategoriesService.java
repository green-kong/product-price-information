package com.example.musinsaserver.category.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.category.application.port.in.GetAllCategoriesUseCase;
import com.example.musinsaserver.category.application.port.in.dto.CategoryResponse;
import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;

@Service
@Transactional(readOnly = true)
public class GetAllCategoriesService implements GetAllCategoriesUseCase {

    private final CategoryRepository categoryRepository;

    public GetAllCategoriesService(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        final List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryResponse::from)
                .toList();
    }
}
