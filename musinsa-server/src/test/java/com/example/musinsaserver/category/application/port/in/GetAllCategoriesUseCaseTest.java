package com.example.musinsaserver.category.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.category.application.port.in.dto.CategoryResponse;
import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.support.BaseTest;

class GetAllCategoriesUseCaseTest extends BaseTest {

    @Autowired
    GetAllCategoriesUseCase getAllCategoriesUseCase;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    @DisplayName("저장된 모든 카테고리를 조회한다.")
    void getAllCategories() {
        //given
        final List<String> categoryNames = List.of("바지", "아우터", "양말", "스니커즈");
        final List<Long> categoryIds = categoryNames.stream()
                .map(name -> categoryRepository.save(Category.createWithoutId(name)))
                .map(Category::getId)
                .toList();

        //when
        final List<CategoryResponse> responses = getAllCategoriesUseCase.getAllCategories();

        //then
        final List<Long> ids = responses.stream()
                .map(CategoryResponse::id)
                .toList();
        final List<String> names = responses.stream()
                .map(CategoryResponse::name)
                .toList();
        assertThat(ids).containsExactlyInAnyOrderElementsOf(categoryIds);
        assertThat(names).containsExactlyInAnyOrderElementsOf(categoryNames);
    }
}
