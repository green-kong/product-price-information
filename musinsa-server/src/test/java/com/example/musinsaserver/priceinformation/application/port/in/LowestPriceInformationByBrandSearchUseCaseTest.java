package com.example.musinsaserver.priceinformation.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MinimumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.support.BaseTest;

class LowestPriceInformationByBrandSearchUseCaseTest extends BaseTest {

    @Autowired
    LowestPriceInformationByBrandSearchUseCase useCase;

    @Autowired
    MinimumPriceInformationRepository informationRepository;

    @Test
    @DisplayName("brandId가 일치하는 모든 최소금액정보를 반환한다.")
    void searchLowestPriceInformationByBrand() {
        //given
        final long targetBrandId = 3L;
        final String targetBrandName = "brandC";
        informationRepository.save(
                PriceInformation.createWithoutId(1L, 13L, targetBrandId, "바지", 10_000, targetBrandName));
        informationRepository.save(PriceInformation.createWithoutId(2L, 13L, 2L, "바지", 20_000, "brandB"));
        informationRepository.save(
                PriceInformation.createWithoutId(3L, 12L, targetBrandId, "액세서리", 30_000, targetBrandName));
        informationRepository.save(
                PriceInformation.createWithoutId(4L, 11L, targetBrandId, "아우터", 1_000, targetBrandName));

        //when
        final LowestPriceInformationByBrandResponses responses = useCase.searchLowestPriceInformationByBrand(
                targetBrandId);

        //then
        assertSoftly(softAssertions -> {
            assertThat(responses.lowestPriceInformationResponses()).hasSize(3);
            assertThat(responses.sum()).isEqualTo(41_000);
            assertThat(responses.brandName()).isEqualTo(targetBrandName);
        });
    }

}
