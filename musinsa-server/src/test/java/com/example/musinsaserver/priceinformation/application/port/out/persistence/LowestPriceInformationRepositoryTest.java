package com.example.musinsaserver.priceinformation.application.port.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.support.BaseTest;

class LowestPriceInformationRepositoryTest extends BaseTest {

    @Autowired
    LowestPriceInformationRepository repository;

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
        final var savedMinimumPriceInformation = repository.save(minimumPriceInformation);

        //then
        final var found = repository.findById(savedMinimumPriceInformation.getId()).get();
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
        final PriceInformation targetInformation = repository.save(
                PriceInformation.createWithoutId(targetProductId, targetCategoryId, targetBrandId, targetCategory,
                        targetPrice,
                        targetBrandName));
        repository.save(
                PriceInformation.createWithoutId(2L, targetCategoryId, 3L, "바지", 30_000, "brandC"));
        repository.save(
                PriceInformation.createWithoutId(3L, targetCategoryId, 4L, "바지", 40_000, "brandD"));
        repository.save(
                PriceInformation.createWithoutId(4L, targetCategoryId, 5L, "바지", 50_000, "brandE"));
        repository.save(PriceInformation.createWithoutId(5L, 4L, 3L, "아우터", 20_000, "brandC"));
        repository.save(
                PriceInformation.createWithoutId(6L, targetCategoryId, 1L, "바지", 20_000, "brandA"));
        repository.save(PriceInformation.createWithoutId(7L, 3L, 2L, "아우터", 20_000, "brandB"));

        //when
        final PriceInformation minimum = repository.findByBrandIdAndCategoryId(targetBrandId,
                targetCategoryId).get();

        //then
        assertThat(minimum.getId()).isEqualTo(targetInformation.getId());
    }

    @Test
    @DisplayName("최소가격 정보를 업데이트한다.")
    void update() {
        //given
        final PriceInformation savedMinimumPriceInformation = repository.save(
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
        repository.updateById(
                savedMinimumPriceInformation.getId(),
                savedMinimumPriceInformation
        );

        //then
        final var found = repository.findById(savedMinimumPriceInformation.getId()).get();
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
        final PriceInformation savedMinimumPriceInformation = repository.save(
                PriceInformation.createWithoutId(
                        productId,
                        categoryId,
                        brandId,
                        category,
                        price,
                        brandName
                ));

        //when
        final PriceInformation found = repository.findByProductId(productId).get();

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
        final Optional<PriceInformation> found = repository.findByProductId(0L);

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
        final PriceInformation savedMinimumPriceInformation = repository.save(
                PriceInformation.createWithoutId(
                        productId,
                        categoryId,
                        brandId,
                        category,
                        price,
                        brandName
                ));

        //when
        repository.deleteById(savedMinimumPriceInformation.getId());

        //then
        final Optional<PriceInformation> found = repository.findByProductId(
                savedMinimumPriceInformation.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("brandId가 일치하는 모든 priceInformation을 반환한다.")
    void findByBrandId() {
        //given
        final long targetBrandId = 3L;
        save(1L, 10L, targetBrandId, "바지", 200, "brandA");
        save(2L, 11L, 4L, "액세서리", 100, "brandB");
        save(3L, 12L, 5L, "아우터", 300, "brandC");
        save(4L, 13L, 6L, "스니커즈", 400, "brandD");
        save(5L, 14L, targetBrandId, "모자", 500, "brandE");
        save(6L, 15L, targetBrandId, "양말", 600, "brandF");

        //when
        final List<PriceInformation> results = repository.findByBrandId(targetBrandId);

        //then
        assertThat(results).hasSize(3);
    }

    private PriceInformation save(
            final long productId,
            final Long categoryId,
            final Long brandId,
            final String category,
            final int price,
            final String brandName
    ) {
        return repository.save(
                PriceInformation.createWithoutId(productId, categoryId, brandId, category, price, brandName));
    }

    @Test
    @DisplayName("category별로 가장 저렴한 가격정보를 반환한다. 가격정보가 존재하지 않는 카테고리에 대해선 반환하지 않는다.")
    void findLowestPriceInformationByCategoryIds() {
        //given
        save(1L, 10L, 3L, "바지", 100, "brandA");
        save(2L, 10L, 4L, "바지", 200, "brandB");
        save(3L, 12L, 3L, "아우터", 300, "brandA");
        save(4L, 12L, 4L, "아우터", 400, "brandB");
        save(5L, 14L, 4L, "모자", 500, "brandB");
        save(6L, 14L, 3L, "모자", 600, "brandA");

        //when
        final List<PriceInformation> result = repository.findLowestPriceInformationByCategoryIds(
                List.of(10L, 12L, 14L, 20L));

        //then
        final List<Integer> prices = result.stream()
                .map(PriceInformation::getPrice)
                .toList();
        final List<Long> productIds = result.stream()
                .map(PriceInformation::getProductId)
                .toList();
        assertThat(result).hasSize(3);
        assertThat(prices).containsExactlyInAnyOrder(100, 300, 500);
        assertThat(productIds).containsExactlyInAnyOrder(1L, 3L, 5L);
    }

    @Test
    @DisplayName("카테고리id가 일치하는 정보중 가장 가격이 저렴한 정보를 반환한다.")
    void findLowestPriceInformationByCategoryId() {
        //given
        final long categoryId = 14L;
        final PriceInformation save = save(6L, categoryId, 3L, "모자", 600, "brandA");
        save(7L, categoryId, 5L, "모자", 700, "brandB");
        save(8L, categoryId, 6L, "모자", 800, "brandC");

        //when
        final PriceInformation found = repository.findEndPriceInformationByCategoryId(categoryId).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(save.getProductId()).isEqualTo(found.getProductId());
            assertThat(save.getBrandName()).isEqualTo(found.getBrandName());
            assertThat(save.getPrice()).isEqualTo(found.getPrice());
        });
    }
}
