package com.example.musinsaserver.brand.application.port.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.brand.domain.Brand;
import com.example.musinsaserver.support.BaseTest;

class BrandRepositoryTest extends BaseTest {

    @Autowired
    BrandRepository brandRepository;

    @Test
    @DisplayName("Brand를 저장한다.")
    void save() {
        //given
        final String name = "name";
        final Brand brand = Brand.createWithoutId(name);

        //when
        final Brand savedBrand = brandRepository.save(brand);

        //then
        assertSoftly(softAssertions -> {
            assertThat(savedBrand.getId()).isNotNull();
            assertThat(savedBrand.getNameValue()).isEqualTo(name);
        });
    }

    @Test
    @DisplayName("저장된 Brand를 Id를 통해 조회한다.")
    void findById() {
        //given
        final String name = "name";
        final Brand brand = Brand.createWithoutId(name);
        final Brand savedBrand = brandRepository.save(brand);

        //when
        final Brand foundBrand = brandRepository.findById(savedBrand.getId()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(foundBrand.getId()).isEqualTo(savedBrand.getId());
            assertThat(foundBrand.getNameValue()).isEqualTo(name);
        });
    }

    @Test
    @DisplayName("id에 해당하는 Brand가 없는 경우 Optional.empty를 반환한다.")
    void findByIdFail() {
        //when
        final Optional<Brand> brand = brandRepository.findById(0L);

        //then
        assertThat(brand).isNotPresent();
    }

    @Test
    @DisplayName("저장된 모든 브랜드 목록을 조회한다.")
    void findAll() {
        //given
        brandRepository.save(Brand.createWithoutId("brandA"));
        brandRepository.save(Brand.createWithoutId("brandB"));
        brandRepository.save(Brand.createWithoutId("brandC"));
        brandRepository.save(Brand.createWithoutId("brandD"));

        //when
        final List<Brand> brands = brandRepository.findAll();

        //then
        assertThat(brands).hasSize(4);
    }

    @Test
    @DisplayName("저장된 브랜드 중 id가 일치하는 모든 브랜드를 조회한다.")
    void findByIds() {
        //given
        final Brand brandA = brandRepository.save(Brand.createWithoutId("brandA"));
        final Brand brandB = brandRepository.save(Brand.createWithoutId("brandB"));
        final Brand brandC = brandRepository.save(Brand.createWithoutId("brandC"));
        final Brand brandD = brandRepository.save(Brand.createWithoutId("brandD"));
        final List<Long> targetIds = List.of(brandA.getId(), brandD.getId(), brandB.getId());

        //when
        final List<Brand> brands = brandRepository.findByIds(targetIds);

        //then
        final List<Long> foundIds = brands.stream()
                .map(Brand::getId)
                .toList();
        assertThat(brands).hasSize(targetIds.size());
        assertThat(foundIds).containsExactlyInAnyOrderElementsOf(targetIds);
    }
}
