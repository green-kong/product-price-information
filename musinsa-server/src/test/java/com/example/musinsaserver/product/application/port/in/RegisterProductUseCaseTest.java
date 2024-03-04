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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;
import com.example.musinsaserver.product.application.port.out.event.ProductRegisterEventPublisher;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.application.port.out.validator.BrandValidator;
import com.example.musinsaserver.product.domain.Category;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.NonExistentBrandException;

@SpringBootTest
class RegisterProductUseCaseTest {

    @Autowired
    RegisterProductUseCase registerProductUseCase;

    @Autowired
    ProductRepository productRepository;

    @MockBean
    BrandValidator brandValidator;

    @MockBean
    ProductRegisterEventPublisher productRegisterEventPublisher;

    @Test
    @DisplayName("product를 저장한다.")
    void registerProduct() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
        doNothing().when(productRegisterEventPublisher).publishRegisterProductEvent(any(ProductRegisterEvent.class));
        final int price = 10_000;
        final Long brandId = 1L;
        final String category = "bag";
        final RegisterProductRequest registerBrandRequest = new RegisterProductRequest(price, category, brandId);

        //when
        final Long savedId = registerProductUseCase.registerProduct(registerBrandRequest);

        //then
        final Product product = productRepository.findById(savedId).get();
        assertSoftly(softAssertions -> {
            assertThat(product.getId()).isEqualTo(savedId);
            assertThat(product.getPriceValue()).isEqualTo(price);
            assertThat(product.getBrandId()).isEqualTo(brandId);
            assertThat(product.getCategory()).isEqualTo(Category.BAG);
        });
    }

    @Test
    @DisplayName("존재하지 않는 brandId를 포함한 product를 저장하면 예외가 발생한다.")
    void registerProductFail() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(false);
        final int price = 10_000;
        final Long brandId = 1L;
        final String category = "bag";
        final RegisterProductRequest registerBrandRequest = new RegisterProductRequest(price, category, brandId);

        //when & then
        assertThatThrownBy(() -> registerProductUseCase.registerProduct(registerBrandRequest))
                .isInstanceOf(NonExistentBrandException.class)
                .hasMessageContaining("프로덕트의 브랜드가 존재하지 않습니다.");
    }
}
