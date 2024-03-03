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
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MinimumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidProductIdException;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;

@SpringBootTest
class MinimumPriceUpdateUseCaseTest {

    @Autowired
    MinimumPriceUpdateUseCase minimumPriceUpdateUseCase;

    @Autowired
    MinimumPriceInformationRepository informationRepository;

    @MockBean
    BrandLoader brandLoader;

    @MockBean
    ProductLoader productLoader;

    @Test
    @DisplayName("새롭게 등록된 product가 기존 minimumPrice보다 저렴한 경우 minimumPrice를 업데이트 한다.")
    void updateMinimumPrice() {
        //given
        final long brandId = 3L;
        final long newProductId = 1L;
        final int newMinimumPrice = 10;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, brandId, newMinimumPrice, "바지"));
        when(productLoader.loadProduct(anyLong())).thenReturn(productLoadDto);

        final PriceInformation currentMinimumPriceInformation = informationRepository.save(
                PriceInformation.createWithoutId(10L, brandId, "바지", 30_000, "brandA")
        );

        //when
        minimumPriceUpdateUseCase.updateMinimumPriceUpdate(new ProductRegisterEvent(newProductId));

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
    @DisplayName("새롭게 등록된 product가 기존 minimumPrice보다 저렴하지 않은 경우 minimumPrice를 업데이트 하지 않는다.")
    void notUpdateMinimumPriceUpdateMoreExpensive() {
        //given
        final long brandId = 3L;
        final Optional<ProductLoadDto> productLoadDto = Optional.of(new ProductLoadDto(1L, brandId, 40_000, "액세서리"));
        when(productLoader.loadProduct(anyLong())).thenReturn(productLoadDto);

        final long originalProductId = 10L;
        final int originalMinimumPrice = 30_000;
        final PriceInformation currentMinimumPriceInformation = informationRepository.save(
                PriceInformation.createWithoutId(originalProductId, brandId, "액세서리", originalMinimumPrice, "brandA")
        );

        //when
        minimumPriceUpdateUseCase.updateMinimumPriceUpdate(new ProductRegisterEvent(1L));

        //then
        final var updatedMinimum = informationRepository.findById(currentMinimumPriceInformation.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(updatedMinimum.getId()).isEqualTo(currentMinimumPriceInformation.getId());
            assertThat(updatedMinimum.getProductId()).isEqualTo(originalProductId);
            assertThat(updatedMinimum.getPrice()).isEqualTo(originalMinimumPrice);
            assertThat(updatedMinimum.getBrandId()).isEqualTo(brandId);
            assertThat(updatedMinimum.getCategory()).isEqualTo("액세서리");
            assertThat(updatedMinimum.getBrandName()).isEqualTo("brandA");
        });
    }

    @Test
    @DisplayName("새롭게 등록된 product의 id가 유효하지 않은 경우 예외가 발생한다.")
    void updateMinimumPriceUpdateFailByInvalidProductId() {
        //given
        final long brandId = 3L;
        when(productLoader.loadProduct(anyLong())).thenReturn(Optional.empty());

        final long originalProductId = 10L;
        final int originalMinimumPrice = 30_000;
        informationRepository.save(
                PriceInformation.createWithoutId(originalProductId, brandId, "액세서리", originalMinimumPrice, "brandA")
        );

        //when & then
        assertThatThrownBy(() -> minimumPriceUpdateUseCase.updateMinimumPriceUpdate(new ProductRegisterEvent(1L)))
                .isInstanceOf(InvalidProductIdException.class)
                .hasMessageContaining("프로덕트 id가 유효하지 않습니다.");
    }

    @Test
    @DisplayName("기존에 등록된 minimumPrice가 없는 경우 새롭게 등록된 product를 minimumPrice로 저장한다.")
    void saveMinimumPrice() {
        //given
        final long brandId = 3L;
        final String brandName = "brandA";
        when(brandLoader.loadBrand(anyLong())).thenReturn(Optional.of(new BrandLoadDto(brandId, brandName)));

        final long newProductId = 1L;
        final int newMinimumPrice = 20_000;
        final String category = "액세서리";
        final Optional<ProductLoadDto> productLoadDto = Optional.of(
                new ProductLoadDto(newProductId, brandId, newMinimumPrice, category));
        when(productLoader.loadProduct(anyLong())).thenReturn(productLoadDto);

        //when
        minimumPriceUpdateUseCase.updateMinimumPriceUpdate(new ProductRegisterEvent(newProductId));

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
                () -> minimumPriceUpdateUseCase.updateMinimumPriceUpdate(new ProductRegisterEvent(newProductId)))
                .isInstanceOf(InvalidBrandIdException.class)
                .hasMessageContaining("유효하지 않은 브랜드 id 입니다.");
    }
}
