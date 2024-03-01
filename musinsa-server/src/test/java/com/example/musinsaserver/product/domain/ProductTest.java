package com.example.musinsaserver.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.musinsaserver.product.exception.InvalidPriceException;

class ProductTest {

    @Test
    @DisplayName("id를 포함하지 않은 Product를 생성한다.")
    void createWithId() {
        //given
        final int price = 100;
        final Long brandId = 1L;

        //when
        final Product product = Product.createWithoutId(price, brandId);

        //then
        assertSoftly(softAssertions -> {
            assertThat(product.getId()).isNull();
            assertThat(product.getPriceValue()).isEqualTo(price);
            assertThat(product.getBrandId()).isEqualTo(brandId);
        });
    }

    @DisplayName("id를 포함하지 않은 product 생성 시, 유효하지 않은 값으로 생성하는 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {9, 1_000_001})
    void createProductWithInvalidValue(final int invalidValue) {
        //given
        final Long brandId = 1L;

        //when & then
        assertThatThrownBy(() -> Product.createWithoutId(invalidValue, brandId))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageContaining("가격은 10원이상 1,000,000원 이하여야 합니다.");
    }

    @Test
    @DisplayName("id를 포함한 Product를 생성한다.")
    void createWithoutId() {
        //given
        final Long id = 1L;
        final int price = 100;
        final Long brandId = 1L;

        //when
        final Product product = Product.createWithId(id, price, brandId);

        //then
        assertSoftly(softAssertions -> {
            assertThat(product.getId()).isEqualTo(id);
            assertThat(product.getPriceValue()).isEqualTo(price);
            assertThat(product.getBrandId()).isEqualTo(brandId);
        });
    }

    @DisplayName("id를 포함한 않은 product 생성 시, 유효하지 않은 값으로 생성하는 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {9, 1_000_001})
    void createProductWithIdAndInvalidValue(final int invalidValue) {
        //given
        final Long id = 1L;
        final Long brandId = 1L;

        //when & then
        assertThatThrownBy(() -> Product.createWithId(id, invalidValue, brandId))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageContaining("가격은 10원이상 1,000,000원 이하여야 합니다.");
    }
}
