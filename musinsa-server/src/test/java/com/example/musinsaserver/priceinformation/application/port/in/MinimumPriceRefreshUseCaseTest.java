package com.example.musinsaserver.priceinformation.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.ProductIsNotDeletedException;
import com.example.musinsaserver.support.BaseTest;

class MinimumPriceRefreshUseCaseTest extends BaseTest {

    @Autowired
    MinimumPriceRefreshUseCase minimumPriceRefreshUseCase;

    @Autowired
    LowestPriceInformationRepository informationRepository;

    @MockBean
    ProductLoader productLoader;

    @Test
    @DisplayName("productId에 해당하는 product가 삭제되지 않은 경우 예외가 발생한다.")
    void refreshFailByProductIsNotDeleted() {
        //given
        final long brandId = 3L;
        final Long categoryId = 19L;
        final long newProductId = 11L;
        final int newPrice = 30_000;
        final ProductLoadDto productLoadDto = new ProductLoadDto(newProductId, brandId, newPrice, categoryId);
        when(productLoader.loadProduct(anyLong())).thenReturn(Optional.of(productLoadDto));

        //when & then
        assertThatThrownBy(() -> minimumPriceRefreshUseCase.refreshMinimumPriceInformation(10L))
                .isInstanceOf(ProductIsNotDeletedException.class)
                .hasMessageContaining("productId에 해당하는 product가 삭제 되지 않았습니다.");
    }

    @Test
    @DisplayName("삭제된 proudct가 minimumPriceInformation이 아닌 경우 원래의 데이터를 유지한다.")
    void maintainCurrentStatus() {
        //given
        final Long productId = 20L;
        final Long categoryId = 13L;
        final Long brandId = 100L;
        final String category = "스니커즈";
        final int price = 20_000;
        final String brandName = "BrandHundred";
        when(productLoader.loadProduct(anyLong())).thenReturn(Optional.empty());
        final PriceInformation savedPriceInformation = informationRepository.save(
                PriceInformation.createWithoutId(productId, categoryId, brandId, category, price, brandName));

        //when
        minimumPriceRefreshUseCase.refreshMinimumPriceInformation(11L);

        //then
        final PriceInformation priceInformation = informationRepository.findById(savedPriceInformation.getId())
                .get();
        assertSoftly(softAssertions -> {
            assertThat(priceInformation.getProductId()).isEqualTo(productId);
            assertThat(priceInformation.getPrice()).isEqualTo(price);
            assertThat(priceInformation.getCategory()).isEqualTo(category);
            assertThat(priceInformation.getBrandId()).isEqualTo(brandId);
            assertThat(priceInformation.getBrandName()).isEqualTo(brandName);
        });
    }

    @Nested
    @DisplayName("삭제된 product가 minimumPriceInformation 중 하나인 경우")
    class containDeletedProduct {

        @Test
        @DisplayName("브랜드와 카테고리가 일치하는 최소가격정보가 있는 경우 업데이트한다.")
        void refreshMinimumPriceInformationRefresh() {
            //given
            final long brandId = 3L;
            final long categoryId = 8L;
            final String category = "가방";
            final String brandName = "brandC";
            final long newProductId = 11L;
            final int newPrice = 30_000;
            final ProductLoadDto productLoadDto = new ProductLoadDto(newProductId, brandId, newPrice, categoryId);

            when(productLoader.loadLowestPriceProductByBrandIdAndCategory(brandId, categoryId)).thenReturn(
                    Optional.of(productLoadDto));
            final PriceInformation savedPriceInformation = informationRepository.save(
                    PriceInformation.createWithoutId(10L, categoryId, brandId, category, 20_000, brandName));

            //when
            minimumPriceRefreshUseCase.refreshMinimumPriceInformation(10L);

            //then
            final PriceInformation priceInformation = informationRepository.findById(savedPriceInformation.getId())
                    .get();
            assertSoftly(softAssertions -> {
                assertThat(priceInformation.getProductId()).isEqualTo(newProductId);
                assertThat(priceInformation.getPrice()).isEqualTo(newPrice);
                assertThat(priceInformation.getCategory()).isEqualTo(category);
                assertThat(priceInformation.getBrandId()).isEqualTo(brandId);
                assertThat(priceInformation.getBrandName()).isEqualTo(brandName);
            });
        }


        @Test
        @DisplayName("브랜드와 카테고리가 일치하는 최소가격정보가 없는 경우 기존 데이터를 삭제한다.")
        void refreshMinimumPriceInformationDelete() {
            //given
            final long brandId = 3L;
            final long categoryId = 1L;
            final String category = "가방";
            final String brandName = "brandC";

            when(productLoader.loadLowestPriceProductByBrandIdAndCategory(brandId, categoryId)).thenReturn(
                    Optional.empty());
            final PriceInformation savedPriceInformation = informationRepository.save(
                    PriceInformation.createWithoutId(10L, categoryId, brandId, category, 20_000, brandName));

            //when
            minimumPriceRefreshUseCase.refreshMinimumPriceInformation(10L);

            //then
            final Optional<PriceInformation> found = informationRepository.findById(savedPriceInformation.getId());
            assertThat(found).isEmpty();
        }
    }
}
