package com.example.musinsaserver.category.adaptor.port.out.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;

@Component
public class InMemoryCategoryRepository implements CategoryRepository {

    private Long insertedId = 0L;
    private final Map<Long, Category> categories = new HashMap<>();

    @Override
    public Category save(final Category category) {
        insertedId += 1;
        final Category categoryWithId = Category.createWithId(insertedId, category.getNameValue());
        categories.put(insertedId, categoryWithId);
        return categoryWithId;
    }

    @Override
    public Optional<Category> findById(final Long id) {
        final Category category = categories.get(id);
        if (Objects.isNull(category)) {
            return Optional.empty();
        }
        return Optional.of(category);
    }

    @Override
    public int countAll() {
        return categories.size();
    }

    public void clear() {
        insertedId = 0L;
        categories.clear();
    }
}
