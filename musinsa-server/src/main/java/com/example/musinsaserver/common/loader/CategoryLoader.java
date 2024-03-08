package com.example.musinsaserver.common.loader;

import java.util.List;
import java.util.Optional;

import com.example.musinsaserver.common.loader.dto.CategoryLoadDto;

public interface CategoryLoader {
    Optional<CategoryLoadDto> loadCategory(final Long categoryId);

    Optional<CategoryLoadDto> loadCategory(final String categoryName);

    int countAllCategories();

    List<CategoryLoadDto> loadAllCategories();

    List<CategoryLoadDto> loadCategoriesByIds(List<Long> categoryIds);
}
