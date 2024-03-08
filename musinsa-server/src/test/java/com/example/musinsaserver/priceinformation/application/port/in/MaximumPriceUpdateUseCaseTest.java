package com.example.musinsaserver.priceinformation.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.common.loader.BrandLoader;
import com.example.musinsaserver.common.loader.CategoryLoader;
import com.example.musinsaserver.common.loader.ProductLoader;
import com.example.musinsaserver.common.loader.dto.BrandLoadDto;
import com.example.musinsaserver.common.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.common.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.HighestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidCategoryIdException;
import com.example.musinsaserver.priceinformation.exception.NonExistentProductWithBrandIdAndCategoryId;
import com.example.musinsaserver.support.BaseTest;

class MaximumPriceUpdateUseCaseTest extends BaseTest {

    @Autowired
    MaximumPriceUpdateUseCase maximumPriceUpdateUseCase;

    @Autowired
    HighestPriceInformationRepository informationRepository;

    @MockBean
    BrandLoader brandLoader;

    @MockBean
    ProductLoader productLoader;

    @MockBean
    CategoryLoader categoryLoader;

    @Test
    @DisplayName("새롭게 등록된 product가 기존 maximumPrice보다 비싼 경우 maximumPrice를 업데이트 한다.")
    void updateMaximumPrice() {
        //given
        final long brandId = 3L;
        final long newProductId = 1L;
        final int newMaximumPrice = 1_000_000;
        final long categoryId = 1L;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, brandId, newMaximumPrice, categoryId));
        when(productLoader.loadHighestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(
                productLoadDto);

        final PriceInformation currentMaximumPriceInformation = informationRepository.save(
                PriceInformation.createWithoutId(10L, categoryId, brandId, "바지", 30_000, "brandA")
        );

        //when
        maximumPriceUpdateUseCase.updateMaximumPrice(brandId, categoryId);

        //then
        final var updatedMaximum = informationRepository.findById(currentMaximumPriceInformation.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(updatedMaximum.getId()).isEqualTo(currentMaximumPriceInformation.getId());
            assertThat(updatedMaximum.getProductId()).isEqualTo(newProductId);
            assertThat(updatedMaximum.getPrice()).isEqualTo(newMaximumPrice);
            assertThat(updatedMaximum.getBrandId()).isEqualTo(brandId);
            assertThat(updatedMaximum.getCategory()).isEqualTo("바지");
            assertThat(updatedMaximum.getBrandName()).isEqualTo("brandA");
        });
    }

    @Test
    @DisplayName("카테고리id와 브랜드id가 일치하는 product가 없는 경우 예외가 발생한다.")
    void updateMaximumPriceUpdateFailByInvalidProductId() {
        //given
        final long brandId = 3L;
        when(productLoader.loadHighestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(
                Optional.empty());

        final long originalProductId = 10L;
        final int originalMaximumPrice = 30_000;
        final long categoryId = 16L;
        informationRepository.save(
                PriceInformation.createWithoutId(originalProductId, brandId, categoryId, "액세서리", originalMaximumPrice,
                        "brandA")
        );

        //when & then
        assertThatThrownBy(() -> maximumPriceUpdateUseCase.updateMaximumPrice(brandId, categoryId))
                .isInstanceOf(NonExistentProductWithBrandIdAndCategoryId.class)
                .hasMessageContaining("categoryId와 brandId에 해당하는 product가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("기존에 등록된 maximumPrice가 없는 경우 새롭게 등록된 product를 maximumPrice로 저장한다.")
    void saveMaximumPrice() {
        //given
        final long brandId = 3L;
        final String brandName = "brandA";
        when(brandLoader.loadBrand(anyLong())).thenReturn(Optional.of(new BrandLoadDto(brandId, brandName)));

        final Long categoryId = 13L;
        final String category = "아우터";
        when(categoryLoader.loadCategory(anyLong())).thenReturn(Optional.of(new CategoryLoadDto(categoryId, category)));

        final long newProductId = 1L;
        final int newMaximumPrice = 20_000;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, brandId, newMaximumPrice, categoryId));
        when(productLoader.loadHighestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(
                productLoadDto);

        //when
        maximumPriceUpdateUseCase.updateMaximumPrice(brandId, categoryId);

        //then
        final var maximum = informationRepository.findByBrandIdAndCategoryId(brandId, categoryId).get();
        assertSoftly(softAssertions -> {
            assertThat(maximum.getProductId()).isEqualTo(newProductId);
            assertThat(maximum.getPrice()).isEqualTo(newMaximumPrice);
            assertThat(maximum.getBrandId()).isEqualTo(brandId);
            assertThat(maximum.getCategoryId()).isEqualTo(categoryId);
            assertThat(maximum.getBrandName()).isEqualTo(brandName);
        });
    }

    @Test
    @DisplayName("새로 등록하려는 정보의 브랜드Id가 유효하지 않은 경우 예외가 발생한다.")
    void saveMaximumPriceFailByInvalidBrandId() {
        //given
        final Long categoryId = 17L;
        final String category = "아우터";
        when(categoryLoader.loadCategory(anyLong())).thenReturn(Optional.of(new CategoryLoadDto(categoryId, category)));

        when(brandLoader.loadBrand(anyLong())).thenReturn(Optional.empty());

        final long newProductId = 1L;
        final int newMaximumPrice = 20_000;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, 3L, newMaximumPrice, categoryId));
        when(productLoader.loadHighestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(
                productLoadDto);

        //when & then
        assertThatThrownBy(
                () -> maximumPriceUpdateUseCase.updateMaximumPrice(0L, categoryId))
                .isInstanceOf(InvalidBrandIdException.class)
                .hasMessageContaining("유효하지 않은 브랜드 id 입니다.");
    }

    @Test
    @DisplayName("새로 등록하려는 정보의 카테고리Id가 유효하지 않은 경우 예외가 발생한다.")
    void saveMaximumPriceFailByInvalidCategoryId() {
        //given
        final long brandId = 3L;
        final String brandName = "brandA";
        when(brandLoader.loadBrand(anyLong())).thenReturn(Optional.of(new BrandLoadDto(brandId, brandName)));

        when(categoryLoader.loadCategory(anyLong())).thenReturn(Optional.empty());

        final long newProductId = 1L;
        final int newMaximumPrice = 20_000;
        final Long categoryId = 14L;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, 3L, newMaximumPrice, categoryId));
        when(productLoader.loadHighestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(
                productLoadDto);

        //when & then
        assertThatThrownBy(
                () -> maximumPriceUpdateUseCase.updateMaximumPrice(brandId, categoryId))
                .isInstanceOf(InvalidCategoryIdException.class)
                .hasMessageContaining("유효하지 않은 name id 입니다.");
    }
}
