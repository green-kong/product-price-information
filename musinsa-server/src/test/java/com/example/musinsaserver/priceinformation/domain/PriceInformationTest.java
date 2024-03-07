package com.example.musinsaserver.priceinformation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PriceInformationTest {

    @Test
    @DisplayName("Id를 포함하지 않은 PriceInformation을 생성한다")
    void createPriceInformationWithoutId() {
        //given
        final long productId = 1L;
        final long brandId = 3L;
        final long categoryId = 6L;
        final String category = "바지";
        final int price = 30_000;
        final String brandName = "D";

        //when
        final PriceInformation priceInformation = PriceInformation.createWithoutId(productId, categoryId, brandId,
                category, price,
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
        final long categoryId = 5L;
        final String category = "바지";
        final int price = 30_000;
        final String brandName = "D";

        //when
        final var priceInformation = PriceInformation.createWithId(id, productId, categoryId, brandId, category, price,
                brandName);

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
        final long categoryId = 10L;
        final String category = "바지";
        final int price = 30_000;
        final String brandName = "D";
        final var priceInformation = PriceInformation.createWithId(id, productId, categoryId, brandId, category, price,
                brandName);

        //when
        priceInformation.update(4L, 10_000);

        //then
        assertThat(priceInformation.getProductId()).isEqualTo(4L);
        assertThat(priceInformation.getPrice()).isEqualTo(10_000);
    }
}
