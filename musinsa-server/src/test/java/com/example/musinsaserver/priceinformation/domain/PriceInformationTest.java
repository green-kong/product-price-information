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
}
