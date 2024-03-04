package com.example.musinsaserver.category.application.port.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.support.BaseTest;

class CategoryRepositoryTest extends BaseTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리를 저장한다.")
    void save() {
        //given
        final String categoryName = "아우터";
        final Category category = Category.createWithoutId(categoryName);

        //when
        final Category savedCategory = categoryRepository.save(category);

        //then
        assertSoftly(softAssertions -> {
            assertThat(savedCategory.getId()).isNotNull();
            assertThat(savedCategory.getNameValue()).isEqualTo(categoryName);
        });
    }

    @Test
    @DisplayName("id가 일치하는 카테고리를 조회한다.")
    void findById() {
        //given
        final String categoryName = "아우터";
        final Category category = Category.createWithoutId(categoryName);
        final Category savedCategory = categoryRepository.save(category);

        //when
        final Category found = categoryRepository.findById(savedCategory.getId()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(found.getId()).isEqualTo(savedCategory.getId());
            assertThat(found.getNameValue()).isEqualTo(categoryName);
        });
    }
    @Test
    @DisplayName("id가 일치하는 카테고리가 없는 경우 Optional.empty를 반환한다.")
    void findByIdReturnEmpty() {
        //when
        final Optional<Category> found = categoryRepository.findById(0L);

        //then
        assertThat(found).isEmpty();
    }
}
