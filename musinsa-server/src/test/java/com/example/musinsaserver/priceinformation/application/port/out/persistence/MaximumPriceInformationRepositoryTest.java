package com.example.musinsaserver.priceinformation.application.port.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

@SpringBootTest
class MaximumPriceInformationRepositoryTest {

    @Autowired
    MaximumPriceInformationRepository maximumPriceInformationRepository;

    @Test
    @DisplayName("최대가격 정보를 저장한다.")
    void save() {
        //given
        final long productId = 1L;
        final long brandId = 3L;
        final long categoryId = 7L;
        final String category = "바지";
        final int price = 20_000;
        final String brandName = "brandA";
        final var maximumPriceInformation = PriceInformation.createWithoutId(
                productId,
                categoryId,
                brandId,
                category,
                price,
                brandName
        );

        //when
        final var savedMaximumPriceInformation = maximumPriceInformationRepository.save(maximumPriceInformation);

        //then
        final var found = maximumPriceInformationRepository.findById(savedMaximumPriceInformation.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(savedMaximumPriceInformation.getId()).isEqualTo(found.getId());
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
        final long targetCategoryId = 27L;
        final String targetCategory = "바지";
        final int targetPrice = 20_000;
        final String targetBrandName = "brandB";
        final PriceInformation targetInformation = maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(targetProductId, targetCategoryId, targetBrandId, targetCategory,
                        targetPrice,
                        targetBrandName));
        maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(2L, targetCategoryId, 3L, "바지", 30_000, "brandC"));
        maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(3L, targetCategoryId, 4L, "바지", 40_000, "brandD"));
        maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(4L, targetCategoryId, 5L, "바지", 50_000, "brandE"));
        maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(5L, targetCategoryId, 3L, "아우터", 20_000, "brandC"));
        maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(6L, targetCategoryId, 1L, "바지", 20_000, "brandA"));
        maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(7L, targetCategoryId, 2L, "아우터", 20_000, "brandB"));

        //when
        final PriceInformation maximum = maximumPriceInformationRepository.findByBrandIdAndCategoryId(targetBrandId,
                targetCategoryId).get();

        //then
        assertThat(maximum.getId()).isEqualTo(targetInformation.getId());
    }

    @Test
    @DisplayName("최가격 정보를 업데이트한다.")
    void update() {
        //given
        final PriceInformation savedMaximumPriceInformation = maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(
                        1L,
                        3L,
                        17L,
                        "바지",
                        20_000,
                        "brandA"
                ));
        final long updatedProductId = 3L;
        final int updatedPrice = 20_000;
        savedMaximumPriceInformation.update(updatedProductId, updatedPrice);

        //when
        maximumPriceInformationRepository.updateById(
                savedMaximumPriceInformation.getId(),
                savedMaximumPriceInformation
        );

        //then
        final var found = maximumPriceInformationRepository.findById(savedMaximumPriceInformation.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(savedMaximumPriceInformation.getId()).isEqualTo(found.getId());
            assertThat(found.getBrandId()).isEqualTo(savedMaximumPriceInformation.getBrandId());
            assertThat(found.getCategory()).isEqualTo(savedMaximumPriceInformation.getCategory());
            assertThat(found.getPrice()).isEqualTo(savedMaximumPriceInformation.getPrice());
            assertThat(found.getBrandName()).isEqualTo(savedMaximumPriceInformation.getBrandName());
        });
    }

    @Test
    @DisplayName("최대가격정보 중 productId가 일치하는 정보를 반환한다.")
    void findByProductId() {
        //given
        final long productId = 10L;
        final long brandId = 3L;
        final long categoryId = 13L;
        final String category = "바지";
        final int price = 20_000;
        final String brandName = "brandA";
        final PriceInformation savedMaximumPriceInformation = maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(
                        productId,
                        categoryId,
                        brandId,
                        category,
                        price,
                        brandName
                ));

        //when
        final PriceInformation found = maximumPriceInformationRepository.findByProductId(productId).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(found.getId()).isEqualTo(savedMaximumPriceInformation.getId());
            assertThat(found.getProductId()).isEqualTo(productId);
            assertThat(found.getBrandId()).isEqualTo(brandId);
            assertThat(found.getCategory()).isEqualTo(category);
            assertThat(found.getPrice()).isEqualTo(price);
            assertThat(found.getBrandName()).isEqualTo(brandName);
        });
    }

    @Test
    @DisplayName("최대가격정보 중 productId가 일치하는 정보가 없는 경우 Optional.empty를 반환한다.")
    void findByProductIdReturnEmpty() {
        //when
        final Optional<PriceInformation> found = maximumPriceInformationRepository.findByProductId(0L);

        //then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("id를 통해 최대가격 정보를 삭제한다.")
    void deleteById() {
        //given
        final long productId = 10L;
        final long brandId = 3L;
        final long categoryId = 19L;
        final String category = "바지";
        final int price = 20_000;
        final String brandName = "brandA";
        final PriceInformation savedMaximumPriceInformation = maximumPriceInformationRepository.save(
                PriceInformation.createWithoutId(
                        productId,
                        brandId,
                        categoryId,
                        category,
                        price,
                        brandName
                ));

        //when
        maximumPriceInformationRepository.deleteById(savedMaximumPriceInformation.getId());

        //then
        final Optional<PriceInformation> found = maximumPriceInformationRepository.findByProductId(
                savedMaximumPriceInformation.getId());
        assertThat(found).isEmpty();
    }
}
