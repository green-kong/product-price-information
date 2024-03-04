package com.example.musinsaserver.priceinformation.adaptor.out.loader;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.priceinformation.application.port.out.loader.CategoryLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.CategoryLoadDto;

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
        if (Objects.isNull(category)) {
            return Optional.empty();
        }
        return Optional.of(new CategoryLoadDto(category.getId(), category.getNameValue()));
    }

    @Override
    public int countAllCategories() {
        return categoryRepository.countAll();
    }
}
