package com.example.musinsaserver.product.domain;

import static com.example.musinsaserver.product.domain.Category.ACCESSORIES;
import static com.example.musinsaserver.product.domain.Category.BAG;
import static com.example.musinsaserver.product.domain.Category.HAT;
import static com.example.musinsaserver.product.domain.Category.OUTER;
import static com.example.musinsaserver.product.domain.Category.PANTS;
import static com.example.musinsaserver.product.domain.Category.SNEAKERS;
import static com.example.musinsaserver.product.domain.Category.SOCKS;
import static com.example.musinsaserver.product.domain.Category.TOP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.example.musinsaserver.product.exception.InvalidCategoryException;

class CategoryTest {

    @DisplayName("대/소문자는 무시하고 이름과 일치하는 Category를 반환한다.")
    @ParameterizedTest
    @MethodSource("provideCategoryAndInput")
    void from(final Category expected, final String input) {
        //when
        final Category result = Category.from(input);

        //then
        assertThat(result).isEqualTo(expected);
    }
    public static Stream<Arguments> provideCategoryAndInput() {
        return Stream.of(
                Arguments.of(TOP, "TOP"),
                Arguments.of(OUTER, "outer"),
                Arguments.of(PANTS, "PaNts"),
                Arguments.of(SNEAKERS, "SNEAkers"),
                Arguments.of(BAG, "bag"),
                Arguments.of(HAT, "hat"),
                Arguments.of(SOCKS, "SoCks"),
                Arguments.of(ACCESSORIES, "accessories")
        );
    }

    @Test
    @DisplayName("이름과 일치하는 cateogry가 없는 경우 예외가 발생한다.")
    void fromWithInvalidName() {
        //given
        final String invalidCategoryName = "invalid";

        //when & then
        assertThatThrownBy(() -> Category.from(invalidCategoryName))
                .isInstanceOf(InvalidCategoryException.class)
                .hasMessageContaining("일치하는 카테고리가 없습니다.");
    }
}
