package com.example.musinsaserver.category.adaptor.out.persistence.jpa;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.category.domain.Category;

@Component
public class JpaCategoryMapper {

    public JpaCategoryEntity toJpaCategoryEntity(final Category category) {
        return new JpaCategoryEntity(category.getNameValue());
    }

    public Category toCategoryDomainEntity(final JpaCategoryEntity jpaCategoryEntity) {
        return Category.createWithId(jpaCategoryEntity.getId(), jpaCategoryEntity.getName());
    }

}
