package com.example.musinsaserver.product.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.musinsaserver.product.exception.InvalidCategoryException;
import com.example.musinsaserver.product.exception.InvalidPriceException;

class ProductTest {

    @Test
    @DisplayName("id를 포함하지 않은 Product를 생성한다.")
    void createWithId() {
        //given
        final int price = 100;
        final Long brandId = 1L;

        //when
        final Category category = Category.ACCESSORIES;
        final Product product = Product.createWithoutId(price, category, brandId);

        //then
        assertSoftly(softAssertions -> {
            assertThat(product.getId()).isNull();
            assertThat(product.getPriceValue()).isEqualTo(price);
            assertThat(product.getBrandId()).isEqualTo(brandId);
            assertThat(product.getCategory()).isEqualTo(category);
        });
    }

    @DisplayName("id를 포함하지 않은 product 생성 시, 유효하지 않은 값으로 생성하는 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {9, 1_000_001})
    void createProductWithInvalidValue(final int invalidValue) {
        //given
        final Long brandId = 1L;
        final Category category = Category.SOCKS;

        //when & then
        assertThatThrownBy(() -> Product.createWithoutId(invalidValue, category, brandId))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageContaining("가격은 10원이상 1,000,000원 이하여야 합니다.");
    }

    @Test
    @DisplayName("id를 포함한 Product를 생성한다.")
    void createWithoutId() {
        //given
        final Long id = 1L;
        final Category category = Category.HAT;
        final int price = 100;
        final Long brandId = 1L;

        //when
        final Product product = Product.createWithId(id, price, category, brandId);

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
        final Category category = Category.HAT;
        final Long brandId = 1L;

        //when & then
        assertThatThrownBy(() -> Product.createWithId(id, invalidValue, category, brandId))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageContaining("가격은 10원이상 1,000,000원 이하여야 합니다.");
    }

    @Test
    @DisplayName("인자로 전달받는 brandId와 일치하면 true를 반환한다.")
    void belongsToSameBrandReturnTrue() {
        //given
        final long brandId = 1L;
        final Product product = Product.createWithoutId(10_000, Category.HAT, brandId);

        //when
        final boolean result = product.belongsToSameBrand(brandId);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("인자로 전달받는 brandId와 일치하지 않으면 false를 반환한다.")
    void belongsToSameBrandReturnFalse() {
        //given
        final long brandId = 1L;
        final Product product = Product.createWithoutId(10_000, Category.HAT, brandId);

        //when
        final boolean result = product.belongsToSameBrand(2L);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("brand의 정보를 변경한다.")
    void update() {
        //given
        final Product product = Product.createWithoutId(10_000, Category.HAT, 1L);

        //when
        product.update(20_000, "accessories", 3L);

        //then
        assertSoftly(softAssertions -> {
            assertThat(product.getCategory()).isEqualTo(Category.ACCESSORIES);
            assertThat(product.getPriceValue()).isEqualTo(20_000);
            assertThat(product.getBrandId()).isEqualTo(2L);
        });
    }

    @Test
    @DisplayName("유효하지 않은 가격으로 변경할 시 예외가 발생한다.")
    void updateFailByInvalidPrice() {
        //given
        final Product product = Product.createWithoutId(10_000, Category.HAT, 1L);

        //when & then
        assertThatThrownBy(() -> product.update(1_000_001, "accessories", 3L))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageContaining("가격은 10원이상 1,000,000원 이하여야 합니다.");
    }

    @Test
    @DisplayName("유효하지 않은 카테고리로 변경할 시 예외가 발생한다.")
    void updateFailByInvalidCategory() {
        //given
        final Product product = Product.createWithoutId(10_000, Category.HAT, 1L);

        //when & then
        assertThatThrownBy(() -> product.update(1_000_000, "invalid", 3L))
                .isInstanceOf(InvalidCategoryException.class)
                .hasMessageContaining("일치하는 카테고리가 없습니다.");
    }
}
