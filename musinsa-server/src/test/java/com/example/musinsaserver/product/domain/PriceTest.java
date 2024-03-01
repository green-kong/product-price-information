package com.example.musinsaserver.product.domain;

import com.example.musinsaserver.product.exception.InvalidPriceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PriceTest {

    @Test
    @DisplayName("Price를 생성한다.")
    void createPrice() {
        //given
        final int priceValue = 10_000;

        //when
        final Price price = Price.from(priceValue);

        //then
        assertThat(price.getValue()).isEqualTo(priceValue);
    }

    @DisplayName("유효하지 않은 값으로 Price를 생성하는 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(ints = {9, 1_000_001})
    void createPriceWithInvalidValue(final int invalidValue) {
        //when & then
        assertThatThrownBy(() -> Price.from(invalidValue))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageContaining("가격은 10원이상 1,000,000원 이하여야 합니다.");
    }
}
