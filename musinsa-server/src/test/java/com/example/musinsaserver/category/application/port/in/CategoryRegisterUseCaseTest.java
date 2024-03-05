package com.example.musinsaserver.category.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.category.application.port.in.dto.CategoryRegisterRequest;
import com.example.musinsaserver.category.application.port.out.persistence.CategoryRepository;
import com.example.musinsaserver.category.domain.Category;
import com.example.musinsaserver.category.exception.DuplicatedCategoryNameException;
import com.example.musinsaserver.support.BaseTest;

class CategoryRegisterUseCaseTest extends BaseTest {

    @Autowired
    CategoryRegisterUseCase categoryRegisterUseCase;

    @Autowired
    CategoryRepository repository;

    @Test
    @DisplayName("카테고리를 저장한다.")
    void save() {
        //given
        final String categoryName = "바지";

        //when
        final Long save = categoryRegisterUseCase.save(new CategoryRegisterRequest(categoryName));

        //then
        final Category found = repository.findByName(categoryName).get();
        assertSoftly(softAssertions -> {
            assertThat(found.getId()).isEqualTo(save);
            assertThat(found.getNameValue()).isEqualTo(categoryName);
        });
    }

    @Test
    @DisplayName("카테고리를 저장한다.")
    void saveFailByDuplicatedName() {
        //given
        final String categoryName = "바지";
        final Long save = categoryRegisterUseCase.save(new CategoryRegisterRequest(categoryName));

        //when & then
        assertThatThrownBy(() -> categoryRegisterUseCase.save(new CategoryRegisterRequest(categoryName)))
                .isInstanceOf(DuplicatedCategoryNameException.class)
                .hasMessageContaining("이미 존재하는 카테고리 이름입니다.");
    }
}
