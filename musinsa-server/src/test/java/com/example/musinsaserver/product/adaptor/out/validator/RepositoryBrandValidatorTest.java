package com.example.musinsaserver.product.adaptor.out.validator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.brand.adaptor.in.validator.RepositoryBrandValidator;
import com.example.musinsaserver.brand.application.port.out.persistence.BrandRepository;
import com.example.musinsaserver.brand.domain.Brand;
import com.example.musinsaserver.support.BaseTest;

class RepositoryBrandValidatorTest extends BaseTest {

    @Autowired
    RepositoryBrandValidator repositoryBrandValidator;

    @Autowired
    BrandRepository brandRepository;

    @Test
    @DisplayName("brandId에 해당하는 브랜드가 없는 경우 false를 반환한다.")
    void isExistedBrandReturnFalse() {
        //when
        final boolean isExisted = repositoryBrandValidator.isExistedBrand(0L);

        //then
        assertThat(isExisted).isFalse();
    }

    @Test
    @DisplayName("brandId에 해당하는 브랜드가 존재하는 경우 true를 반환한다.")
    void isExistedBrandReturnTrue() {
        //given
        final Brand brand = brandRepository.save(Brand.createWithoutId("brnadA"));

        //when
        final boolean isExisted = repositoryBrandValidator.isExistedBrand(brand.getId());

        //then
        assertThat(isExisted).isTrue();
    }
}
