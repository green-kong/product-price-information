package com.example.musinsaserver.category.adaptor.port.out.persistence.jpa;

import static java.util.Objects.isNull;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;

@Component
public class JpaCategoryRepository implements CategoryRepository {
    private final JpaCategoryAdaptor categories;
    private final JpaCategoryMapper mapper;

    public JpaCategoryRepository(final JpaCategoryAdaptor categories, final JpaCategoryMapper mapper) {
        this.categories = categories;
        this.mapper = mapper;
    }

    @Override
    public Category save(final Category category) {
        final JpaCategoryEntity jpaCategoryEntity = mapper.toJpaCategoryEntity(category);
        categories.save(jpaCategoryEntity);
        return mapper.toCategoryDomainEntity(jpaCategoryEntity);
    }

    @Override
    public Optional<Category> findById(final Long id) {
        final JpaCategoryEntity jpaCategoryEntity = categories.findById(id)
                .orElse(null);
        if (isNull(jpaCategoryEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toCategoryDomainEntity(jpaCategoryEntity));
    }

    @Override
    public int countAll() {
        return (int) categories.count();
    }
}
