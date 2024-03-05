package com.example.musinsaserver.priceinformation.application.port.out.persistence;

import static com.example.musinsaserver.priceinformation.domain.PriceInformation.createWithoutId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.support.BaseTest;

class HighestPriceInformationRepositoryTest extends BaseTest {

    @Autowired
    HighestPriceInformationRepository repositoy;

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
        final var maximumPriceInformation = createWithoutId(
                productId,
                categoryId,
                brandId,
                category,
                price,
                brandName
        );

        //when
        final var savedMaximumPriceInformation = repositoy.save(maximumPriceInformation);

        //then
        final var found = repositoy.findById(savedMaximumPriceInformation.getId()).get();
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
        final long targetBrandId = 3L;
        final long targetCategoryId = 2L;
        final var saved = saveHighest(1L, targetCategoryId, targetBrandId, "바지", 20_000, "A");
        saveHighest(1L, targetCategoryId, 6L, "바지", 20_000, "B");
        saveHighest(1L, 3L, targetBrandId, "아우터", 20_000, "A");
        saveHighest(1L, 4L, 7L, "양말", 20_000, "C");

        //when
        final var found = repositoy.findByBrandIdAndCategoryId(targetBrandId, targetCategoryId).get();

        //then
        assertThat(saved.getId()).isEqualTo(found.getId());
    }

    private PriceInformation saveHighest(
            final long productId,
            final Long categoryId,
            final Long brandId,
            final String category,
            final int price,
            final String brandName
    ) {
        return repositoy.save(createWithoutId(
                productId,
                categoryId,
                brandId,
                category,
                price,
                brandName
        ));
    }

    @Test
    @DisplayName("최대 가격 정보를 업데이트한다.")
    void update() {
        //given
        final PriceInformation savedMaximumPriceInformation = repositoy.save(
                createWithoutId(
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
        repositoy.updateById(
                savedMaximumPriceInformation.getId(),
                savedMaximumPriceInformation
        );

        //then
        final var found = repositoy.findById(savedMaximumPriceInformation.getId()).get();
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
        final PriceInformation savedMaximumPriceInformation = repositoy.save(
                createWithoutId(
                        productId,
                        categoryId,
                        brandId,
                        category,
                        price,
                        brandName
                ));

        //when
        final PriceInformation found = repositoy.findByProductId(productId).get();

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
        final Optional<PriceInformation> found = repositoy.findByProductId(0L);

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
        final PriceInformation savedMaximumPriceInformation = repositoy.save(
                createWithoutId(
                        productId,
                        brandId,
                        categoryId,
                        category,
                        price,
                        brandName
                ));

        //when
        repositoy.deleteById(savedMaximumPriceInformation.getId());

        //then
        final Optional<PriceInformation> found = repositoy.findByProductId(
                savedMaximumPriceInformation.getId());
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("categoryId가 일치하는 정보중 가격이 제일 비싼 정보를 반환한다.")
    void findHighestByCategoryId() {
        //given
        final Long targetCategoryId = 1L;
        saveHighest(1L, targetCategoryId, 6L, "바지", 20_000, "b");
        saveHighest(2L, targetCategoryId, 7L, "바지", 30_000, "c");
        saveHighest(3L, targetCategoryId, 8L, "바지", 40_000, "d");
        final PriceInformation target = saveHighest(4L, targetCategoryId, 9L, "바지", 50_000, "f");

        //when
        final PriceInformation priceInformation = repositoy.findEndPriceInformationByCategoryId(targetCategoryId).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(priceInformation.getProductId()).isEqualTo(4L);
            assertThat(priceInformation.getCategoryId()).isEqualTo(targetCategoryId);
            assertThat(priceInformation.getBrandId()).isEqualTo(9L);
            assertThat(priceInformation.getPrice()).isEqualTo(50_000);
        });
    }
}
