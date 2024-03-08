package com.example.musinsaserver.product.adaptor.out.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.category.adaptor.in.validator.RepositoryCategoryValidator;
import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.support.BaseTest;

class RepositoryCategoryValidatorTest extends BaseTest {

    @Autowired
    RepositoryCategoryValidator repositoryCategoryValidator;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("categoryId에 해당하는 카테고리가 없는경우 false를 반환한다.")
    void isExistedCategoryReturnFalse() {
        //when
        final boolean result = repositoryCategoryValidator.isExistedCategory(0L);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("categoryId에 해당하는 카테고리가 존재하는 경우 true를 반환한다.")
    void isExistedCategoryReturnTrue() {
        //given
        final Category category = categoryRepository.save(Category.createWithoutId("양말"));

        //when
        final boolean result = repositoryCategoryValidator.isExistedCategory(category.getId());

        //then
        assertThat(result).isTrue();
    }

}
