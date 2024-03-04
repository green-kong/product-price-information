package com.example.musinsaserver.category.domain;

import static com.example.musinsaserver.category.domain.Category.createWithId;
import static com.example.musinsaserver.category.domain.Category.createWithoutId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.musinsaserver.category.exception.InvalidCategoryNameException;

class CategoryTest {

    @Test
    @DisplayName("id를 포함하지 않은 카테고리를 생성한다.")
    void createCategoryWithoutId() {
        //given
        final String value = "액세서리";

        //when
        final Category category = createWithoutId(value);

        //then
        assertSoftly(softAssertions -> {
            assertThat(category.getId()).isNull();
            assertThat(category.getNameValue()).isEqualTo(value);
        });
    }

    @Test
    @DisplayName("id를 포함한 않은 카테고리를 생성한다.")
    void createCategoryWithId() {
        //given
        final long id = 1L;
        final String value = "액세서리";

        //when
        final Category category = createWithId(id, value);

        //then
        assertSoftly(softAssertions -> {
            assertThat(category.getId()).isEqualTo(id);
            assertThat(category.getNameValue()).isEqualTo(value);
        });
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 값으로 id를 포함하지 않은 category를 생성하면 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"b", "", "  "})
    void createWithoutIdFail(final String invalidName) {
        //when & then
        assertThatThrownBy(() -> createWithoutId(invalidName))
                .isInstanceOf(InvalidCategoryNameException.class)
                .hasMessageContaining("카테고리명은 2글자 이상이어야 합니다.");
    }

    @ParameterizedTest
    @DisplayName("유효하지 않은 값으로 id를 포함한 category를 생성하면 예외가 발생한다.")
    @NullAndEmptySource
    @ValueSource(strings = {"b", "", "  "})
    void createWithIdFail(final String invalidName) {
        //when & then
        assertThatThrownBy(() -> createWithId(1L, invalidName))
                .isInstanceOf(InvalidCategoryNameException.class)
                .hasMessageContaining("카테고리명은 2글자 이상이어야 합니다.");
    }
}
