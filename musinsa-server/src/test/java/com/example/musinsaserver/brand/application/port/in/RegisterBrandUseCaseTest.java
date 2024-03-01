package com.example.musinsaserver.brand.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.musinsaserver.brand.application.port.in.dto.RegisterBrandRequest;
import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;

@SpringBootTest
class RegisterBrandUseCaseTest {

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
}
