package com.example.musinsaserver.product.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;
import com.example.musinsaserver.product.application.port.out.event.ProductRegisterEventPublisher;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.application.port.out.validator.BrandValidator;
import com.example.musinsaserver.product.application.port.out.validator.CategoryValidator;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.NonExistentBrandException;
import com.example.musinsaserver.product.exception.NonExistentCategoryException;
import com.example.musinsaserver.support.BaseTest;

class RegisterProductUseCaseTest extends BaseTest {

    @Autowired
    RegisterProductUseCase registerProductUseCase;

    @Autowired
    ProductRepository productRepository;

    @MockBean
    BrandValidator brandValidator;

    @MockBean
    CategoryValidator categoryValidator;

    @MockBean
    ProductRegisterEventPublisher productRegisterEventPublisher;

    @Test
    @DisplayName("product를 저장한다.")
    void registerProduct() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
        when(categoryValidator.isExistedCategory(anyLong())).thenReturn(true);
        doNothing().when(productRegisterEventPublisher).publishRegisterProductEvent(any(ProductRegisterEvent.class));
        final int price = 10_000;
        final Long brandId = 1L;
        final Long categoryId = 1L;
        final RegisterProductRequest registerBrandRequest = new RegisterProductRequest(price, categoryId, brandId);

        //when
        final Long savedId = registerProductUseCase.registerProduct(registerBrandRequest);

        //then
        final Product product = productRepository.findById(savedId).get();
        assertSoftly(softAssertions -> {
            assertThat(product.getId()).isEqualTo(savedId);
            assertThat(product.getPriceValue()).isEqualTo(price);
            assertThat(product.getBrandId()).isEqualTo(brandId);
            assertThat(product.getCategoryId()).isEqualTo(categoryId);
        });
    }

    @Test
    @DisplayName("존재하지 않는 brandId를 포함한 product를 저장하면 예외가 발생한다.")
    void registerProductFailByInvalidBrandId() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(false);
        when(categoryValidator.isExistedCategory(anyLong())).thenReturn(true);
        final int price = 10_000;
        final Long brandId = 1L;
        final Long categoryId = 3L;
        final RegisterProductRequest registerBrandRequest = new RegisterProductRequest(price, categoryId, brandId);

        //when & then
        assertThatThrownBy(() -> registerProductUseCase.registerProduct(registerBrandRequest))
                .isInstanceOf(NonExistentBrandException.class)
                .hasMessageContaining("프로덕트의 브랜드가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 categoryId를 포함한 product를 저장하면 예외가 발생한다.")
    void registerProductFailByInvalidCategoryId() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
        when(categoryValidator.isExistedCategory(anyLong())).thenReturn(false);
        final int price = 10_000;
        final Long brandId = 1L;
        final Long categoryId = 3L;
        final RegisterProductRequest registerBrandRequest = new RegisterProductRequest(price, categoryId, brandId);

        //when & then
        assertThatThrownBy(() -> registerProductUseCase.registerProduct(registerBrandRequest))
                .isInstanceOf(NonExistentCategoryException.class)
                .hasMessageContaining("일치하는 카테고리가 없습니다.");
    }
}
