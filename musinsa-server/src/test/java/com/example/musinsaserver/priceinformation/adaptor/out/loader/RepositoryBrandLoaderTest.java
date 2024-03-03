package com.example.musinsaserver.priceinformation.adaptor.out.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;
import com.example.musinsaserver.priceinformation.application.port.out.loader.BrandLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.BrandLoadDto;

@SpringBootTest
class RepositoryBrandLoaderTest {

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

}
