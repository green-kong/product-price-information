package com.example.musinsaserver.brand.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.musinsaserver.brand.exception.InvalidBrandNameException;

class BrandTest {

    @Test
    @DisplayName("Id 없이 Brand를 생성한다.")
    void createWithoutId() {
        //given
        final String brandName = "brandName";

        //when
        final Brand brand = Brand.createWithoutId(brandName);

        //then
        assertSoftly(softAssertions -> {
            assertThat(brand.getId()).isNull();
            assertThat(brand.getNameValue()).isEqualTo(brandName);
        });
    }

    @DisplayName("유효하지 않은 브랜드명으로 Id를 포함하지 않은 Brand를 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "  "})
    void createWithoutIdAndInvalidName(final String invalidName) {
        //when & then
        assertThatThrownBy(() -> Brand.createWithoutId(invalidName))
                .isInstanceOf(InvalidBrandNameException.class)
                .hasMessageContaining("상품의 이름은 한글자 이상이어야 합니다.");
    }

    @Test
    @DisplayName("Id를 포함한 Brand를 생성한다.")
    void createWithId() {
        //given
        final Long id = 1L;
        final String brandName = "brandName";

        //when
        final Brand brand = Brand.createWithId(id, brandName);

        //then
        assertSoftly(softAssertions -> {
            assertThat(brand.getId()).isEqualTo(id);
            assertThat(brand.getNameValue()).isEqualTo(brandName);
        });
    }

    @DisplayName("유효하지 않은 브랜드명으로 아이디를 포함한 Brand를 생성하면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "  "})
    void createWithIdAndInvalidName(final String invalidName) {
        //when & then
        assertThatThrownBy(() -> Brand.createWithoutId(invalidName))
                .isInstanceOf(InvalidBrandNameException.class)
                .hasMessageContaining("상품의 이름은 한글자 이상이어야 합니다.");
    }
}
