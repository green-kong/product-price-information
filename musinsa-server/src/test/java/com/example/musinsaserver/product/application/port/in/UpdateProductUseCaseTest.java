package com.example.musinsaserver.product.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.product.application.port.in.dto.ProductUpdateRequest;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.application.port.out.validator.BrandValidator;
import com.example.musinsaserver.product.domain.Category;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.InvalidCategoryException;
import com.example.musinsaserver.product.exception.InvalidPriceException;
import com.example.musinsaserver.product.exception.NonExistentBrandException;
import com.example.musinsaserver.product.exception.NonExistentProductException;

@SpringBootTest
class UpdateProductUseCaseTest {

    @Autowired
    UpdateProductUseCase updateProductUseCase;

    @Autowired
    ProductRepository productRepository;

    @MockBean
    BrandValidator brandValidator;

    @Test
    @DisplayName("프로덕트의 정보를 업데이트 한다.")
    void update() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
        final Product savedProduct = productRepository.save(Product.createWithoutId(10, Category.ACCESSORIES, 1L));

        final int updatedPrice = 1_000_000;
        final String updatedCategory = "hat";
        final Long updatedBrandId = 3L;
        final var productUpdateRequest = new ProductUpdateRequest(updatedPrice, updatedCategory, updatedBrandId);

        //when
        updateProductUseCase.updateProduct(savedProduct.getId(), productUpdateRequest);

        //then
        final Product product = productRepository.findById(savedProduct.getId()).get();
        assertThat(product.getId()).isEqualTo(savedProduct.getId());
        assertThat(product.getPriceValue()).isEqualTo(updatedPrice);
        assertThat(product.getBrandId()).isEqualTo(updatedBrandId);
        assertThat(product.getCategory()).isEqualTo(Category.HAT);
    }

    @Test
    @DisplayName("존재하지 않는 프로덕트의 정보를 업데이트 하는 경우 예외가 발생한다.")
    void updateFailByInvalidProductId() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);

        final int updatedPrice = 1_000_000;
        final String updatedCategory = "hat";
        final Long updatedBrandId = 3L;
        final var productUpdateRequest = new ProductUpdateRequest(updatedPrice, updatedCategory, updatedBrandId);

        //when & then
        assertThatThrownBy(() -> updateProductUseCase.updateProduct(0L, productUpdateRequest))
                .isInstanceOf(NonExistentProductException.class)
                .hasMessageContaining("존재하지 않는 프로덕트입니다.");
    }

    @Test
    @DisplayName("프로덕트의 정보를 유효하지 않은 가격으로 업데이트 하는 경우 예외가 발생한다.")
    void updateFailByInvalidPrice() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
        final Product savedProduct = productRepository.save(Product.createWithoutId(10, Category.ACCESSORIES, 1L));

        final int updatedPrice = 1_000_001;
        final String updatedCategory = "hat";
        final Long updatedBrandId = 3L;
        final var productUpdateRequest = new ProductUpdateRequest(updatedPrice, updatedCategory, updatedBrandId);

        //when & then
        assertThatThrownBy(() -> updateProductUseCase.updateProduct(savedProduct.getId(), productUpdateRequest))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageContaining("가격은 10원이상 1,000,000원 이하여야 합니다.");
    }

    @Test
    @DisplayName("프로덕트의 정보를 유효하지 않은 카테고리로 업데이트 하는 경우 예외가 발생한다.")
    void updateFailByInvalidCategory() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(true);
        final Product savedProduct = productRepository.save(Product.createWithoutId(10, Category.ACCESSORIES, 1L));

        final int updatedPrice = 1_000_000;
        final String updatedCategory = "invalid";
        final Long updatedBrandId = 3L;
        final var productUpdateRequest = new ProductUpdateRequest(updatedPrice, updatedCategory, updatedBrandId);

        //when & then
        assertThatThrownBy(() -> updateProductUseCase.updateProduct(savedProduct.getId(), productUpdateRequest))
                .isInstanceOf(InvalidCategoryException.class)
                .hasMessageContaining("일치하는 카테고리가 없습니다.");
    }


    @Test
    @DisplayName("프로덕트의 정보를 유효하지 않은 카테고리로 업데이트 하는 경우 예외가 발생한다.")
    void updateFailByInvalidBrandId() {
        //given
        when(brandValidator.isExistedBrand(anyLong())).thenReturn(false);
        final Product savedProduct = productRepository.save(Product.createWithoutId(10, Category.ACCESSORIES, 1L));

        final int updatedPrice = 1_000_000;
        final String updatedCategory = "invalid";
        final Long updatedBrandId = 3L;
        final var productUpdateRequest = new ProductUpdateRequest(updatedPrice, updatedCategory, updatedBrandId);

        //when & then
        assertThatThrownBy(() -> updateProductUseCase.updateProduct(savedProduct.getId(), productUpdateRequest))
                .isInstanceOf(NonExistentBrandException.class)
                .hasMessageContaining("프로덕트의 브랜드가 존재하지 않습니다.");
    }
}
