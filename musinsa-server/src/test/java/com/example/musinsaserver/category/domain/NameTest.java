package com.example.musinsaserver.category.domain;

import static com.example.musinsaserver.category.domain.Name.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.musinsaserver.category.exception.InvalidCategoryNameException;

class NameTest {

    @Test
    @DisplayName("카테고리의 이름을 생성한다.")
    void createCategoryName() {
        //given
        final String value = "바지";

        //when
        final Name name = from(value);

        //then
        assertThat(name.getValue()).isEqualTo(value);
    }

    @DisplayName("두글자 미만의 값으로 카테고리네임을 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"a", "", "  "})
    void createCategoryNameFail(final String invalidValue) {
        //when & then
        assertThatThrownBy(() -> from(invalidValue))
                .isInstanceOf(InvalidCategoryNameException.class)
                .hasMessageContaining("카테고리명은 2글자 이상이어야 합니다.");
    }
}
