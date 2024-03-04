package com.example.musinsaserver.priceinformation.application.port.out.loader;

import java.util.Optional;

import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.CategoryLoadDto;

public interface CategoryLoader {
    Optional<CategoryLoadDto> loadCategory(final Long categoryId);

    int countAllCategories();
}
