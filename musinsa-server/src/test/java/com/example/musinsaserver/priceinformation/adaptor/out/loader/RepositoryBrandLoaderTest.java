package com.example.musinsaserver.priceinformation.adaptor.out.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;
import com.example.musinsaserver.common.loader.BrandLoader;
import com.example.musinsaserver.common.loader.dto.BrandLoadDto;
import com.example.musinsaserver.support.BaseTest;

class RepositoryBrandLoaderTest extends BaseTest {

    @Autowired
    BrandLoader brandLoader;

    @Autowired
    BrandRepository brandRepository;

    @Test
    @DisplayName("id를 통해 브랜드정보를 불러온다.")
    void loadBrand() {
        //given
        final Brand brand = brandRepository.save(Brand.createWithoutId("brandA"));

        //when
        final BrandLoadDto brandLoadDto = brandLoader.loadBrand(brand.getId()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(brandLoadDto.brandId()).isEqualTo(brand.getId());
            assertThat(brandLoadDto.brandName()).isEqualTo(brand.getNameValue());
        });
    }

    @Test
    @DisplayName("id와 일치하는 브랜드가 없는경우 empty를 반환한다.")
    void loadBrandReturnEmpty() {
        //when
        final Optional<BrandLoadDto> brandLoadDto = brandLoader.loadBrand(0L);

        //then
        assertThat(brandLoadDto).isEmpty();
    }

    @Test
    @DisplayName("저장된 브랜드 중 id가 일치하는 모든 브랜드를 조회한다.")
    void findByIds() {
        //given
        final Brand brandA = brandRepository.save(Brand.createWithoutId("brandA"));
        final Brand brandB = brandRepository.save(Brand.createWithoutId("brandB"));
        final Brand brandC = brandRepository.save(Brand.createWithoutId("brandC"));
        final Brand brandD = brandRepository.save(Brand.createWithoutId("brandD"));
        final List<Long> targetIds = List.of(brandD.getId(), brandC.getId(), brandB.getId());

        //when
        final List<BrandLoadDto> brandLoadDtos = brandLoader.loadBrandByIds(targetIds);

        //then
        final List<Long> foundIds = brandLoadDtos.stream()
                .map(BrandLoadDto::brandId)
                .toList();
        assertThat(brandLoadDtos).hasSize(targetIds.size());
        assertThat(foundIds).containsExactlyInAnyOrderElementsOf(targetIds);
    }
}
