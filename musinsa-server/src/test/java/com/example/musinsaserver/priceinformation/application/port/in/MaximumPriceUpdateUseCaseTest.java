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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.priceinformation.application.port.out.loader.BrandLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.BrandLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MaximumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidProductIdException;

@SpringBootTest
class MaximumPriceUpdateUseCaseTest {

    @Autowired
    MaximumPriceUpdateUseCase maximumPriceUpdateUseCase;

    @Autowired
    MaximumPriceInformationRepository informationRepository;

    @MockBean
    BrandLoader brandLoader;

    @MockBean
    ProductLoader productLoader;

    @Test
    @DisplayName("새롭게 등록된 product가 기존 maximumPrice보다 비싼 경우 maximumPrice를 업데이트 한다.")
    void updateMinimumPrice() {
        //given
        final long brandId = 3L;
        final long newProductId = 1L;
        final int newMaximumPrice = 1_000_000;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, brandId, newMaximumPrice, "바지"));
        when(productLoader.loadProduct(anyLong())).thenReturn(productLoadDto);

        final PriceInformation currentMaximumPriceInformation = informationRepository.save(
                PriceInformation.createWithoutId(10L, brandId, "바지", 30_000, "brandA")
        );

        //when
        maximumPriceUpdateUseCase.updateMaximumPrice(newProductId);

        //then
        final var updatedMinimum = informationRepository.findById(currentMaximumPriceInformation.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(updatedMinimum.getId()).isEqualTo(currentMaximumPriceInformation.getId());
            assertThat(updatedMinimum.getProductId()).isEqualTo(newProductId);
            assertThat(updatedMinimum.getPrice()).isEqualTo(newMaximumPrice);
            assertThat(updatedMinimum.getBrandId()).isEqualTo(brandId);
            assertThat(updatedMinimum.getCategory()).isEqualTo("바지");
            assertThat(updatedMinimum.getBrandName()).isEqualTo("brandA");
        });
    }

    @Test
    @DisplayName("새롭게 등록된 product가 기존 maximumPrice보다 비싸지 않은 경우 maximumPrice를 업데이트 하지 않는다.")
    void notUpdateMaximumPriceUpdateMoreExpensive() {
        //given
        final long brandId = 3L;
        final String category = "아우터";
        final Optional<ProductLoadDto> productLoadDto = Optional.of(new ProductLoadDto(1L, brandId, 10, category));
        when(productLoader.loadProduct(anyLong())).thenReturn(productLoadDto);

        final long originalProductId = 10L;
        final int originalMinimumPrice = 30_000;
        final PriceInformation currentMinimumPriceInformation = informationRepository.save(
                PriceInformation.createWithoutId(originalProductId, brandId, category, originalMinimumPrice, "brandA")
        );

        //when
        maximumPriceUpdateUseCase.updateMaximumPrice(1L);

        //then
        final var updatedMinimum = informationRepository.findById(currentMinimumPriceInformation.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(updatedMinimum.getId()).isEqualTo(currentMinimumPriceInformation.getId());
            assertThat(updatedMinimum.getProductId()).isEqualTo(originalProductId);
            assertThat(updatedMinimum.getPrice()).isEqualTo(originalMinimumPrice);
            assertThat(updatedMinimum.getBrandId()).isEqualTo(brandId);
            assertThat(updatedMinimum.getCategory()).isEqualTo(category);
            assertThat(updatedMinimum.getBrandName()).isEqualTo("brandA");
        });
    }

    @Test
    @DisplayName("새롭게 등록된 product의 id가 유효하지 않은 경우 예외가 발생한다.")
    void updateMaximumPriceUpdateFailByInvalidProductId() {
        //given
        final long brandId = 3L;
        when(productLoader.loadProduct(anyLong())).thenReturn(Optional.empty());

        final long originalProductId = 10L;
        final int originalMinimumPrice = 30_000;
        informationRepository.save(
                PriceInformation.createWithoutId(originalProductId, brandId, "액세서리", originalMinimumPrice, "brandA")
        );

        //when & then
        assertThatThrownBy(() -> maximumPriceUpdateUseCase.updateMaximumPrice(1L))
                .isInstanceOf(InvalidProductIdException.class)
                .hasMessageContaining("프로덕트 id가 유효하지 않습니다.");
    }

    @Test
    @DisplayName("기존에 등록된 maximumPrice가 없는 경우 새롭게 등록된 product를 maximumPrice로 저장한다.")
    void saveMinimumPrice() {
        //given
        final long brandId = 3L;
        final String brandName = "brandA";
        when(brandLoader.loadBrand(anyLong())).thenReturn(Optional.of(new BrandLoadDto(brandId, brandName)));

        final long newProductId = 1L;
        final int newMinimumPrice = 20_000;
        final String category = "모자";
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, brandId, newMinimumPrice, category));
        when(productLoader.loadProduct(anyLong())).thenReturn(productLoadDto);

        //when
        maximumPriceUpdateUseCase.updateMaximumPrice(newProductId);

        //then
        final var minimum = informationRepository.findByBrandIdAndCategory(brandId, category).get();
        assertSoftly(softAssertions -> {
            assertThat(minimum.getProductId()).isEqualTo(newProductId);
            assertThat(minimum.getPrice()).isEqualTo(newMinimumPrice);
            assertThat(minimum.getBrandId()).isEqualTo(brandId);
            assertThat(minimum.getCategory()).isEqualTo(category);
            assertThat(minimum.getBrandName()).isEqualTo(brandName);
        });
    }

    @Test
    @DisplayName("새로 등록하려는 정보의 브랜드Id가 유효하지 않은 경우 예외가 발생한다.")
    void saveMinimumPriceFailByInvalidBrandId() {
        //given
        when(brandLoader.loadBrand(anyLong())).thenReturn(Optional.empty());

        final long newProductId = 1L;
        final int newMinimumPrice = 20_000;
        final String category = "액세서리";
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, 3L, newMinimumPrice, category));
        when(productLoader.loadProduct(anyLong())).thenReturn(productLoadDto);

        //when & then
        assertThatThrownBy(
                () -> maximumPriceUpdateUseCase.updateMaximumPrice(newProductId))
                .isInstanceOf(InvalidBrandIdException.class)
                .hasMessageContaining("유효하지 않은 브랜드 id 입니다.");
    }
}
