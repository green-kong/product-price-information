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
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidCategoryIdException;
import com.example.musinsaserver.priceinformation.exception.NonExistentProductWithBrandIdAndCategoryId;
import com.example.musinsaserver.support.BaseTest;

class MinimumPriceUpdateUseCaseTest extends BaseTest {

    @Autowired
    MinimumPriceUpdateUseCase minimumPriceUpdateUseCase;

    @Autowired
    LowestPriceInformationRepository informationRepository;

    @MockBean
    BrandLoader brandLoader;

    @MockBean
    ProductLoader productLoader;

    @MockBean
    CategoryLoader categoryLoader;

    @Test
    @DisplayName("새롭게 등록된 product가 기존 minimumPrice보다 저렴한 경우 minimumPrice를 업데이트 한다.")
    void updateMinimumPrice() {
        //given
        final long brandId = 3L;
        final long newProductId = 1L;
        final int newMinimumPrice = 10;
        final long categoryId = 14L;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, brandId, newMinimumPrice, categoryId));
        when(productLoader.loadLowestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(productLoadDto);

        final PriceInformation currentMinimumPriceInformation = informationRepository.save(
                PriceInformation.createWithoutId(10L, categoryId, brandId, "바지", 30_000, "brandA")
        );

        //when
        minimumPriceUpdateUseCase.updateMinimumPrice(brandId, categoryId);

        //then
        final var updatedMinimum = informationRepository.findById(currentMinimumPriceInformation.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(updatedMinimum.getId()).isEqualTo(currentMinimumPriceInformation.getId());
            assertThat(updatedMinimum.getProductId()).isEqualTo(newProductId);
            assertThat(updatedMinimum.getPrice()).isEqualTo(newMinimumPrice);
            assertThat(updatedMinimum.getBrandId()).isEqualTo(brandId);
            assertThat(updatedMinimum.getCategory()).isEqualTo("바지");
            assertThat(updatedMinimum.getBrandName()).isEqualTo("brandA");
        });
    }

    @Test
    @DisplayName("brnadId와 categoryId가 일치하는 product를 찾지 못한 경우 예외가 발생한다.")
    void updateMinimumPriceUpdateFailByInvalidProductId() {
        //given
        when(productLoader.loadHighestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(
                Optional.empty());

        //when & then
        assertThatThrownBy(() -> minimumPriceUpdateUseCase.updateMinimumPrice(1L, 2L))
                .isInstanceOf(NonExistentProductWithBrandIdAndCategoryId.class)
                .hasMessageContaining("categoryId와 brandId에 해당하는 product가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("기존에 등록된 minimumPrice가 없는 경우 새롭게 등록된 product를 minimumPrice로 저장한다.")
    void saveMinimumPrice() {
        //given
        final Long categoryId = 7L;
        final String category = "양말";
        when(categoryLoader.loadCategory(anyLong())).thenReturn(Optional.of(new CategoryLoadDto(categoryId, category)));

        final long brandId = 3L;
        final String brandName = "brandA";
        when(brandLoader.loadBrand(anyLong())).thenReturn(Optional.of(new BrandLoadDto(brandId, brandName)));

        final long newProductId = 1L;
        final int newMinimumPrice = 20_000;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, brandId, newMinimumPrice, categoryId));
        when(productLoader.loadLowestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(productLoadDto);

        //when
        minimumPriceUpdateUseCase.updateMinimumPrice(brandId, categoryId);

        //then
        final var minimum = informationRepository.findByBrandIdAndCategoryId(brandId, categoryId).get();
        assertSoftly(softAssertions -> {
            assertThat(minimum.getProductId()).isEqualTo(newProductId);
            assertThat(minimum.getPrice()).isEqualTo(newMinimumPrice);
            assertThat(minimum.getBrandId()).isEqualTo(brandId);
            assertThat(minimum.getCategoryId()).isEqualTo(categoryId);
            assertThat(minimum.getBrandName()).isEqualTo(brandName);
        });
    }

    @Test
    @DisplayName("새로 등록하려는 정보의 브랜드Id가 유효하지 않은 경우 예외가 발생한다.")
    void saveMinimumPriceFailByInvalidBrandId() {
        //given
        final Long categoryId = 25L;
        final String category = "양말";
        when(categoryLoader.loadCategory(anyLong())).thenReturn(Optional.of(new CategoryLoadDto(categoryId, category)));

        when(brandLoader.loadBrand(anyLong())).thenReturn(Optional.empty());

        final long newProductId = 1L;
        final int newMinimumPrice = 20_000;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, 3L, newMinimumPrice, categoryId));
        when(productLoader.loadLowestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(productLoadDto);

        //when & then
        assertThatThrownBy(
                () -> minimumPriceUpdateUseCase.updateMinimumPrice(0L, categoryId))
                .isInstanceOf(InvalidBrandIdException.class)
                .hasMessageContaining("유효하지 않은 브랜드 id 입니다.");
    }

    @Test
    @DisplayName("새로 등록하려는 정보의 카테고리Id가 유효하지 않은 경우 예외가 발생한다.")
    void saveMinimumPriceFailByInvalidCategoryId() {
        //given
        when(categoryLoader.loadCategory(anyLong())).thenReturn(Optional.empty());

        final long brandId = 3L;
        final String brandName = "brandA";
        when(brandLoader.loadBrand(anyLong())).thenReturn(Optional.of(new BrandLoadDto(brandId, brandName)));

        final long newProductId = 1L;
        final int newMinimumPrice = 20_000;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, brandId, newMinimumPrice, 2L));
        when(productLoader.loadLowestPriceProductByBrandIdAndCategory(anyLong(), anyLong())).thenReturn(productLoadDto);

        //when & then
        assertThatThrownBy(
                () -> minimumPriceUpdateUseCase.updateMinimumPrice(brandId, 0L))
                .isInstanceOf(InvalidCategoryIdException.class)
                .hasMessageContaining("유효하지 않은 name id 입니다.");
    }
}
