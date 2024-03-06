package com.example.musinsaserver.brand.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.brand.application.port.in.dto.BrandResponse;
import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;
import com.example.musinsaserver.support.BaseTest;

class GetAllBrandsUseCaseTest extends BaseTest {

    @Autowired
    GetAllBrandsUseCase useCase;

    @Autowired
    BrandRepository brandRepository;

    @Test
    @DisplayName("저장된 모든 브랜드를 가져온다.")
    void getAllBrands() {
        //given
        brandRepository.save(Brand.createWithoutId("A"));
        brandRepository.save(Brand.createWithoutId("B"));
        brandRepository.save(Brand.createWithoutId("C"));
        brandRepository.save(Brand.createWithoutId("D"));

        //when
        final List<BrandResponse> allBrands = useCase.getAllBrands();

        //then
        final List<String> names = allBrands.stream()
                .map(BrandResponse::name)
                .toList();
        assertSoftly(softAssertions -> {
            assertThat(allBrands).hasSize(4);
            assertThat(names).containsExactlyInAnyOrder("A", "B", "C", "D");
        });

    }
}
