package com.example.musinsaserver.priceinformation.application.port.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;

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
        final long categoryId = 3L;
        final long brandId = 3L;
        final String category = "바지";
        final int price = 20_000;
        final String brandName = "brandA";
        final var minimumPriceInformation = PriceInformation.createWithoutId(
                productId,
                categoryId,
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
        final long targetCategoryId = 6L;
        final long targetBrandId = 2L;
        final String targetCategory = "바지";
        final int targetPrice = 20_000;
        final String targetBrandName = "brandB";
        final PriceInformation targetInformation = minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(targetProductId, targetCategoryId, targetBrandId, targetCategory,
                        targetPrice,
                        targetBrandName));
        minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(2L, targetCategoryId, 3L, "바지", 30_000, "brandC"));
        minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(3L, targetCategoryId, 4L, "바지", 40_000, "brandD"));
        minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(4L, targetCategoryId, 5L, "바지", 50_000, "brandE"));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(5L, 4L, 3L, "아우터", 20_000, "brandC"));
        minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(6L, targetCategoryId, 1L, "바지", 20_000, "brandA"));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(7L, 3L, 2L, "아우터", 20_000, "brandB"));

        //when
        final PriceInformation minimum = minimumPriceInformationRepository.findByBrandIdAndCategoryId(targetBrandId,
                targetCategoryId).get();

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
                        4L,
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

    @Test
    @DisplayName("최소가격정보 중 productId가 일치하는 정보를 반환한다.")
    void findByProductId() {
        //given
        final long productId = 10L;
        final long categoryId = 4L;
        final long brandId = 3L;
        final String category = "바지";
        final int price = 20_000;
        final String brandName = "brandA";
        final PriceInformation savedMinimumPriceInformation = minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(
                        productId,
                        categoryId,
                        brandId,
                        category,
                        price,
                        brandName
                ));

        //when
        final PriceInformation found = minimumPriceInformationRepository.findByProductId(productId).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(found.getId()).isEqualTo(savedMinimumPriceInformation.getId());
            assertThat(found.getProductId()).isEqualTo(productId);
            assertThat(found.getBrandId()).isEqualTo(brandId);
            assertThat(found.getCategory()).isEqualTo(category);
            assertThat(found.getPrice()).isEqualTo(price);
            assertThat(found.getBrandName()).isEqualTo(brandName);
        });
    }

    @Test
    @DisplayName("최소가격정보 중 productId가 일치하는 정보가 없는 경우 Optional.empty를 반환한다.")
    void findByProductIdReturnEmpty() {
        //when
        final Optional<PriceInformation> found = minimumPriceInformationRepository.findByProductId(0L);

        //then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("id를 통해 최소가격 정보를 삭제한다.")
    void deleteById() {
        //given
        final long productId = 10L;
        final long categoryId = 4L;
        final long brandId = 3L;
        final String category = "바지";
        final int price = 20_000;
        final String brandName = "brandA";
        final PriceInformation savedMinimumPriceInformation = minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(
                        productId,
                        categoryId,
                        brandId,
                        category,
                        price,
                        brandName
                ));

        //when
        minimumPriceInformationRepository.deleteById(savedMinimumPriceInformation.getId());

        //then
        final Optional<PriceInformation> found = minimumPriceInformationRepository.findByProductId(
                savedMinimumPriceInformation.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("brandId가 일치하는 모든 priceInformation을 반환한다.")
    void findByBrandId() {
        //given
        final long targetBrandId = 41L;

        minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(1L, 24L, targetBrandId, "바지", 10_000, "brandB"));
        minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(2L,10L, targetBrandId, "액세서리", 10_000, "brandB"));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(3L,24L, 4L, "바지", 10_000, "brandD"));
        minimumPriceInformationRepository.save(PriceInformation.createWithoutId(4L,24L, 3L, "바지", 10_000, "brandC"));
        minimumPriceInformationRepository.save(
                PriceInformation.createWithoutId(5L, 11L, targetBrandId, "아우터", 10_000, "brandB"));

        //when
        final List<PriceInformation> results = minimumPriceInformationRepository.findByBrandId(targetBrandId);

        //then
        assertThat(results).hasSize(3);
    }
}
