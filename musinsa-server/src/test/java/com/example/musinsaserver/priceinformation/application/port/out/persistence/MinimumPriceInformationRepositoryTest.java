package com.example.musinsaserver.priceinformation.application.port.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

@SpringBootTest
class MinimumPriceInformationRepositoryTest {

    @Autowired
    MinimumPriceInformationRepository minimumPriceInformationRepository;

    @Test
    @DisplayName("최소가격 정보를 저장한다.")
    void save() {
        //given
        final long productId = 1L;
        final long brandId = 3L;
        final String category = "바지";
        final int price = 20_000;
        final String brandName = "brandA";
        final var minimumPriceInformation = PriceInformation.createWithoutId(
                productId,
                brandId,
                category,
                price,
                brandName
        );

        //when
        final var savedMinimumPriceInformation = minimumPriceInformationRepository.save(minimumPriceInformation);

        //then
        final var found = minimumPriceInformationRepository.findById(savedMinimumPriceInformation.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(savedMinimumPriceInformation.getId()).isEqualTo(found.getId());
            assertThat(found.getBrandId()).isEqualTo(brandId);
            assertThat(found.getCategory()).isEqualTo(category);
            assertThat(found.getPrice()).isEqualTo(price);
            assertThat(found.getBrandName()).isEqualTo(brandName);
        });
    }

    @Test
    @DisplayName("브랜드id와 카테고리로 가격정보를 조회한다.")
    void findByBrandIdAndCategory() {
        //given
        final long targetProductId = 1L;
        final long targetBrandId = 2L;
        final String targetCategory = "바지";
        final int targetPrice = 20_000;
        final String targetBrandName = "brandB";
        final PriceInformation targetInformation = minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(targetProductId, targetBrandId, targetCategory, targetPrice,
                        targetBrandName));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(2L, 3L, "바지", 30_000, "brandC"));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(3L, 4L, "바지", 40_000, "brandD"));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(4L, 5L, "바지", 50_000, "brandE"));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(5L, 3L, "아우터", 20_000, "brandC"));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(6L, 1L, "바지", 20_000, "brandA"));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(7L, 2L, "아우터", 20_000, "brandB"));

        //when
        final PriceInformation minimum = minimumPriceInformationRepository.findByBrandIdAndCategory(targetBrandId,
                targetCategory).get();

        //then
        assertThat(minimum.getId()).isEqualTo(targetInformation.getId());
    }

    @Test
    @DisplayName("최소가격 정보를 업데이트한다.")
    void update() {
        //given
        final PriceInformation savedMinimumPriceInformation = minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(
                        1L,
                        3L,
                        "바지",
                        20_000,
                        "brandA"
                ));
        final long updatedProductId = 3L;
        final int updatedPrice = 20_000;
        savedMinimumPriceInformation.update(updatedProductId, updatedPrice);

        //when
        minimumPriceInformationRepository.updateById(
                savedMinimumPriceInformation.getId(),
                savedMinimumPriceInformation
        );

        //then
        final var found = minimumPriceInformationRepository.findById(savedMinimumPriceInformation.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(savedMinimumPriceInformation.getId()).isEqualTo(found.getId());
            assertThat(found.getBrandId()).isEqualTo(savedMinimumPriceInformation.getBrandId());
            assertThat(found.getCategory()).isEqualTo(savedMinimumPriceInformation.getCategory());
            assertThat(found.getPrice()).isEqualTo(savedMinimumPriceInformation.getPrice());
            assertThat(found.getBrandName()).isEqualTo(savedMinimumPriceInformation.getBrandName());
        });
    }
}
