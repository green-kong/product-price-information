package com.example.musinsaserver.common.adaptor.out.loader;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.common.application.port.out.loader.CategoryLoader;
import com.example.musinsaserver.common.application.port.out.loader.dto.CategoryLoadDto;

@Component
public class RepositoryCategoryLoader implements CategoryLoader {

    private final CategoryRepository categoryRepository;

    public RepositoryCategoryLoader(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<CategoryLoadDto> loadCategory(final Long categoryId) {
        final Category category = categoryRepository.findById(categoryId)
                .orElse(null);
        if (isNull(category)) {
            return Optional.empty();
        }
        return Optional.of(new CategoryLoadDto(category.getId(), category.getNameValue()));
    }

    @Override
    public int countAllCategories() {
        return categoryRepository.countAll();
    }

    @Override
    public List<CategoryLoadDto> loadAllCategories() {
        final List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(category -> new CategoryLoadDto(category.getId(), category.getNameValue()))
                .toList();
    }

    @Override
    public List<CategoryLoadDto> loadCategoriesByIds(final List<Long> categoryIds) {
        return categoryRepository.findByIds(categoryIds)
                .stream()
                .map(category -> new CategoryLoadDto(category.getId(), category.getNameValue()))
                .toList();
    }

    @Override
    public Optional<CategoryLoadDto> loadCategory(final String categoryName) {
        final Category category = categoryRepository.findByName(categoryName)
                .orElse(null);
        if (isNull(category)) {
            return Optional.empty();
        }
        return Optional.of(new CategoryLoadDto(category.getId(), category.getNameValue()));
    }
}
