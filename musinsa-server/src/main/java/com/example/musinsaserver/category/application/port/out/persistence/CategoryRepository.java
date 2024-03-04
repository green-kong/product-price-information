package com.example.musinsaserver.category.application.port.out.persistence;

import java.util.Optional;

import com.example.musinsaserver.category.domain.Category;

public interface CategoryRepository {
    Category save(final Category category);

    Optional<Category> findById(final Long id);

    int countAll();
}
