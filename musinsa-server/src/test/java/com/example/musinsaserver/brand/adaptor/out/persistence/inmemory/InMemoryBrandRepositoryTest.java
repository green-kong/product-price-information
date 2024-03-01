package com.example.musinsaserver.brand.adaptor.out.persistence.inmemory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.musinsaserver.brand.domain.Brand;

class InMemoryBrandRepositoryTest {

    InMemoryBrandRepository inMemoryBrandRepository;

    @BeforeEach
    void setUp() {
        inMemoryBrandRepository = new InMemoryBrandRepository();
    }

    @Test
    @DisplayName("Brand를 저장한다.")
    void save() {
        //given
        final String name = "name";
        final Brand brand = Brand.createWithoutId(name);

        //when
        final Brand savedBrand = inMemoryBrandRepository.save(brand);

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
        final Brand savedBrand = inMemoryBrandRepository.save(brand);

        //when
        final Brand foundBrand = inMemoryBrandRepository.findById(savedBrand.getId()).get();

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
        final Optional<Brand> brand = inMemoryBrandRepository.findById(0L);

        //then
        assertThat(brand).isNotPresent();
    }
}
