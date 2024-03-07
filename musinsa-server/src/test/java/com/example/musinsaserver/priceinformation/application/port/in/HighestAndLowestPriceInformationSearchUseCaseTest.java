package com.example.musinsaserver.priceinformation.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.common.application.port.out.loader.CategoryLoader;
import com.example.musinsaserver.common.application.port.out.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.HighestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.NonExistentCategoryNameException;
import com.example.musinsaserver.priceinformation.exception.NonExistentHighestInformation;
import com.example.musinsaserver.priceinformation.exception.NonExistentLowestInformation;
import com.example.musinsaserver.support.BaseTest;

class HighestAndLowestPriceInformationSearchUseCaseTest extends BaseTest {

    @Autowired
    HighestAndLowestPriceInformationSearchUseCase useCase;

    @Autowired
    HighestPriceInformationRepository highestRepository;

    @Autowired
    LowestPriceInformationRepository lowestRepository;

    @MockBean
    CategoryLoader categoryLoader;

    @Test
    @DisplayName("카테고리가 일치하는 정보 중 가장 비싼 가격의 정보와 가장 저렴한 정보를 반환한다.")
    void searchHighestAndLowestPriceInformation() {
        //given
        final String categoryName = "스니커즈";
        final CategoryLoadDto category = new CategoryLoadDto(1L, categoryName);
        when(categoryLoader.loadCategory(anyString())).thenReturn(Optional.of(category));

        highestRepository.save(PriceInformation.createWithoutId(1L, 1L, 5L, "스니커즈", 30_000, "brandA"));
        highestRepository.save(PriceInformation.createWithoutId(2L, 1L, 6L, "스니커즈", 20_000, "brandB"));
        highestRepository.save(PriceInformation.createWithoutId(3L, 1L, 7L, "스니커즈", 25_000, "brandC"));
        highestRepository.save(PriceInformation.createWithoutId(4L, 2L, 8L, "양말", 50_000, "brandD"));

        lowestRepository.save(PriceInformation.createWithoutId(5L, 1L, 9L, "스니커즈", 1_000, "brandE"));
        lowestRepository.save(PriceInformation.createWithoutId(6L, 1L, 10L, "스니커즈", 2_000, "brandF"));
        lowestRepository.save(PriceInformation.createWithoutId(7L, 1L, 11L, "스니커즈", 3_000, "brandG"));
        lowestRepository.save(PriceInformation.createWithoutId(8L, 2L, 12L, "양말", 5_000, "brandH"));

        //when
        final var response = useCase.searchHighestAndLowestPriceInformation(categoryName);

        //then
        assertSoftly(softAssertions -> {
            assertThat(response.category()).isEqualTo(categoryName);
            assertThat(response.lowest().brand()).isEqualTo("brandE");
            assertThat(response.lowest().price()).isEqualTo(1_000);
            assertThat(response.highest().brand()).isEqualTo("brandA");
            assertThat(response.highest().price()).isEqualTo(30_000);
        });
    }

    @Test
    @DisplayName("카테고리가 존재하지 않는 경우 예외가 발생한다.")
    void searchHighestAndLowestPriceInformationWithInvalidCategory() {
        //given
        when(categoryLoader.loadCategory(anyString())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> useCase.searchHighestAndLowestPriceInformation("invalid"))
                .isInstanceOf(NonExistentCategoryNameException.class)
                .hasMessageContaining("존재하지 않는 카테고리 입니다.");
    }

    @Test
    @DisplayName("카테고리가 일치하는 최소가격정보가 없는 경우 예외가 발생한다.")
    void searchHighestAndLowestPriceInformationWithouLowest() {
        //given
        final String categoryName = "스니커즈";
        final CategoryLoadDto category = new CategoryLoadDto(1L, categoryName);
        when(categoryLoader.loadCategory(anyString())).thenReturn(Optional.of(category));

        highestRepository.save(PriceInformation.createWithoutId(1L, 1L, 5L, "스니커즈", 30_000, "brandA"));
        highestRepository.save(PriceInformation.createWithoutId(2L, 1L, 6L, "스니커즈", 20_000, "brandB"));
        highestRepository.save(PriceInformation.createWithoutId(3L, 1L, 7L, "스니커즈", 25_000, "brandC"));
        highestRepository.save(PriceInformation.createWithoutId(4L, 2L, 8L, "양말", 50_000, "brandD"));

        lowestRepository.save(PriceInformation.createWithoutId(8L, 2L, 12L, "양말", 5_000, "brandH"));

        //when & then
        assertThatThrownBy(() -> useCase.searchHighestAndLowestPriceInformation(categoryName))
                .isInstanceOf(NonExistentLowestInformation.class)
                .hasMessageContaining("categoyId에 해당하는 최소 가격정보가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("카테고리가 일치하는 최대가격정보가 없는 경우 예외가 발생한다.")
    void searchHighestAndLowestPriceInformationWithoutHighest() {
        //given
        final String categoryName = "스니커즈";
        final CategoryLoadDto category = new CategoryLoadDto(1L, categoryName);
        when(categoryLoader.loadCategory(anyString())).thenReturn(Optional.of(category));

        highestRepository.save(PriceInformation.createWithoutId(4L, 2L, 8L, "양말", 50_000, "brandD"));

        lowestRepository.save(PriceInformation.createWithoutId(5L, 1L, 9L, "스니커즈", 1_000, "brandE"));
        lowestRepository.save(PriceInformation.createWithoutId(6L, 1L, 10L, "스니커즈", 2_000, "brandF"));
        lowestRepository.save(PriceInformation.createWithoutId(7L, 1L, 11L, "스니커즈", 3_000, "brandG"));
        lowestRepository.save(PriceInformation.createWithoutId(8L, 2L, 12L, "양말", 5_000, "brandH"));

        //when & then
        assertThatThrownBy(() -> useCase.searchHighestAndLowestPriceInformation(categoryName))
                .isInstanceOf(NonExistentHighestInformation.class)
                .hasMessageContaining("categoyId에 해당하는 최대 가격정보가 존재하지 않습니다.");
    }
}
