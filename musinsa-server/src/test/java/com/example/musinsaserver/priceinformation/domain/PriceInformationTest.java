package com.example.musinsaserver.priceinformation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PriceInformationTest {

    @Test
    @DisplayName("Id를 포함하지 않은 PriceInformation을 생성한다")
    void createPriceInformationWithoutId() {
        //given
        final long productId = 1L;
        final long brandId = 3L;
        final String category = "바지";
        final int price = 30_000;
        final String brandName = "D";

        //when
        final PriceInformation priceInformation = PriceInformation.createWithoutId(productId, brandId, category, price,
                brandName);

        //then
        assertSoftly(softAssertions -> {
            assertThat(priceInformation.getProductId()).isEqualTo(productId);
            assertThat(priceInformation.getBrandId()).isEqualTo(brandId);
            assertThat(priceInformation.getCategory()).isEqualTo(category);
            assertThat(priceInformation.getPrice()).isEqualTo(price);
            assertThat(priceInformation.getBrandName()).isEqualTo(brandName);
        });
    }

    @Test
    @DisplayName("Id를 포함하지 않은 PriceInformation을 생성한다")
    void createPriceInformationWithId() {
        //given
        final Long id = 2L;
        final Long productId = 1L;
        final Long brandId = 3L;
        final String category = "바지";
        final int price = 30_000;
        final String brandName = "D";

        //when
        final var priceInformation = PriceInformation.createWithId(id, productId, brandId, category, price, brandName);

        //then
        assertSoftly(softAssertions -> {
            assertThat(priceInformation.getId()).isEqualTo(id);
            assertThat(priceInformation.getProductId()).isEqualTo(productId);
            assertThat(priceInformation.getBrandId()).isEqualTo(brandId);
            assertThat(priceInformation.getCategory()).isEqualTo(category);
            assertThat(priceInformation.getPrice()).isEqualTo(price);
            assertThat(priceInformation.getBrandName()).isEqualTo(brandName);
        });
    }

    @Test
    @DisplayName("priceInformation의 가격과 productId를 변경한다.")
    void update() {
        //given
        final Long id = 2L;
        final Long productId = 1L;
        final Long brandId = 3L;
        final String category = "바지";
        final int price = 30_000;
        final String brandName = "D";
        final var priceInformation = PriceInformation.createWithId(id, productId, brandId, category, price, brandName);

        //when
        priceInformation.update(4L, 10_000);

        //then
        assertThat(priceInformation.getProductId()).isEqualTo(4L);
        assertThat(priceInformation.getPrice()).isEqualTo(10_000);
    }

    @DisplayName("priceInformation의 price가 인자로 받은 price보다 크면 true를 반환하고, 같거나 작으면 false를 반환한다.")
    @ParameterizedTest
    @MethodSource("provideComparedPriceAndExpected")
    void isMoreExpensiveThanReturn(final int comparedPrice, final boolean expected) {
        //given
        final Long id = 2L;
        final Long productId = 1L;
        final Long brandId = 3L;
        final String category = "바지";
        final int price = 30_000;
        final String brandName = "D";
        final var priceInformation = PriceInformation.createWithId(id, productId, brandId, category, price, brandName);

        //when
        final boolean result = priceInformation.isMoreExpensiveThan(comparedPrice);

        //then
        assertThat(result).isEqualTo(expected);
    }

    public static Stream<Arguments> provideComparedPriceAndExpected() {
        return Stream.of(
                Arguments.of(29_999, true),
                Arguments.of(30_000, false)
        );
    }

    @DisplayName("priceInformation의 price가 인자로 받은 price보다 작으면 true를 반환하고, 같거나 크면 false를 반환한다.")
    @ParameterizedTest
    @MethodSource("provideComparedPriceAndExpectedForMaximum")
    void isMoreCheaperThanReturn(final int comparedPrice, final boolean expected) {
        //given
        final Long id = 2L;
        final Long productId = 1L;
        final Long brandId = 3L;
        final String category = "바지";
        final int price = 30_000;
        final String brandName = "D";
        final var priceInformation = PriceInformation.createWithId(id, productId, brandId, category, price, brandName);

        //when
        final boolean result = priceInformation.isMoreExpensiveThan(comparedPrice);

        //then
        assertThat(result).isEqualTo(expected);
    }

    public static Stream<Arguments> provideComparedPriceAndExpectedForMaximum() {
        return Stream.of(
                Arguments.of(29_999, true),
                Arguments.of(30_000, false),
                Arguments.of(30_001, false)
        );
    }
}
