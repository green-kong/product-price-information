package com.example.musinsaserver.brand.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;
import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;
import com.example.musinsaserver.brand.exception.DuplicatedBrandNameException;
import com.example.musinsaserver.support.BaseTest;

class RegisterBrandUseCaseTest extends BaseTest {

    @Autowired
    RegisterBrandUseCase registerBrandUseCase;

    @Autowired
    BrandRepository brandRepository;

    @Test
    @DisplayName("브랜드를 등록한다.")
    void registerBrand() {
        //given
        final String brandName = "name";
        final RegisterBrandRequest registerBrandRequest = new RegisterBrandRequest(brandName);

        //when
        final Long savedId = registerBrandUseCase.registerBrand(registerBrandRequest);

        //then
        final Brand brand = brandRepository.findById(savedId).get();
        assertSoftly(softAssertions -> {
            assertThat(brand.getId()).isNotNull();
            assertThat(brand.getNameValue()).isEqualTo(brandName);
        });
    }

    @Test
    @DisplayName("중복된 브랜드명이 있으면 예외가 발생한다.")
    void createBrandFailByDuplicatedName() {
        //given
        final String brandName = "name";
        final RegisterBrandRequest registerBrandRequest = new RegisterBrandRequest(brandName);
        registerBrandUseCase.registerBrand(registerBrandRequest);

        //when & then
        assertThatThrownBy(() -> registerBrandUseCase.registerBrand(registerBrandRequest))
                .isInstanceOf(DuplicatedBrandNameException.class)
                .hasMessageContaining("이미 존재하는 브랜드 명입니다.");

    }
}
