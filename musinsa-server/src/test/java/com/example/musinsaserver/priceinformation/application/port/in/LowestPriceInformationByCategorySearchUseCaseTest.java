package com.example.musinsaserver.priceinformation.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.priceinformation.application.port.out.loader.CategoryLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InsufficientDataByCategoryException;
import com.example.musinsaserver.support.BaseTest;

class LowestPriceInformationByCategorySearchUseCaseTest extends BaseTest {

    @Autowired
    LowestPriceInformationByCategorySearchUseCase useCase;

    @Autowired
    LowestPriceInformationRepository repository;

    @MockBean
    CategoryLoader categoryLoader;

    @Test
    @DisplayName("카테고리별 가장 저렴한 금액의 상품정보를 반환한다.")
    void searchLowestPriceByCategory() {
        //given
        final CategoryLoadDto pants = new CategoryLoadDto(2L, "바지");
        final CategoryLoadDto hat = new CategoryLoadDto(3L, "모자");
        final CategoryLoadDto socks = new CategoryLoadDto(5L, "양말");
        final CategoryLoadDto accessories = new CategoryLoadDto(6L, "액세서리");
        when(categoryLoader.loadAllCategories()).thenReturn(List.of(pants, hat, socks, accessories));
        getSave(1L, 2L, 1L, "바지", 10, "brandA");
        getSave(2L, 2L, 2L, "바지", 20, "brandB");
        getSave(3L, 3L, 3L, "모자", 30, "brandC");
        getSave(4L, 3L, 4L, "모자", 40, "brandD");
        getSave(5L, 5L, 5L, "양말", 50, "brandE");
        getSave(6L, 6L, 6L, "액세서리", 60, "brandF");

        //when
        final var result = useCase.searchLowestPriceInformationByCategory();

        //then
        assertSoftly(softAssertions -> {
            assertThat(result.sum()).isEqualTo(150);
            assertThat(result.responses()).hasSize(4);
        });
    }

    @Test
    @DisplayName("카테고리별 가장 저렴한 금액의 상품정보의 갯수가 전체 카테고리갯수와 일치하지 않는경우 예외가 발생한다..")
    void searchLowestPriceByCategoryFail() {
        //given
        final CategoryLoadDto pants = new CategoryLoadDto(2L, "바지");
        final CategoryLoadDto hat = new CategoryLoadDto(3L, "모자");
        final CategoryLoadDto socks = new CategoryLoadDto(5L, "양말");
        final CategoryLoadDto bag = new CategoryLoadDto(9L, "가방");
        final CategoryLoadDto accessories = new CategoryLoadDto(6L, "액세서리");
        when(categoryLoader.loadAllCategories()).thenReturn(List.of(pants, hat, socks, accessories, bag));
        getSave(1L, 2L, 1L, "바지", 10, "brandA");
        getSave(2L, 2L, 2L, "바지", 20, "brandB");
        getSave(3L, 3L, 3L, "모자", 30, "brandC");
        getSave(4L, 3L, 4L, "모자", 40, "brandD");
        getSave(5L, 5L, 5L, "양말", 50, "brandE");
        getSave(6L, 6L, 6L, "액세서리", 60, "brandF");

        //when & then
        assertThatThrownBy(() -> useCase.searchLowestPriceInformationByCategory())
                .isInstanceOf(InsufficientDataByCategoryException.class)
                .hasMessageContaining("최소가격정보가 존재하지 않는 카테고리가 존재합니다.");
    }

    private PriceInformation getSave(
            final Long productId,
            final Long categoryId,
            final Long brandId,
            final String category,
            final int price,
            final String brandName
    ) {
        return repository.save(
                PriceInformation.createWithoutId(productId, categoryId, brandId, category, price, brandName));
    }

}
