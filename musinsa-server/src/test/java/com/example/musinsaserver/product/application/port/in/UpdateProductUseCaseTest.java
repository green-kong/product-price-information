package com.example.musinsaserver.product.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.product.application.port.in.dto.ProductPriceUpdateRequest;
import com.example.musinsaserver.product.application.port.out.event.ProductUpdateEventPublisher;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductUpdateEvent;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.InvalidPriceException;
import com.example.musinsaserver.product.exception.NonExistentProductException;

@SpringBootTest
class UpdateProductUseCaseTest {

    @Autowired
    UpdateProductUseCase updateProductUseCase;

    @Autowired
    ProductRepository productRepository;

    @MockBean
    ProductUpdateEventPublisher publisher;

    @Test
    @DisplayName("프로덕트의 정보를 업데이트 한다.")
    void update() {
        //given
        doNothing().when(publisher).publishUpdateProductEvent(any(ProductUpdateEvent.class));
        final Product savedProduct = productRepository.save(Product.createWithoutId(10, 4L, 1L));

        final int updatedPrice = 1_000_000;
        final var productUpdateRequest = new ProductPriceUpdateRequest(updatedPrice);

        //when
        updateProductUseCase.updateProductPrice(savedProduct.getId(), productUpdateRequest);

        //then
        verify(publisher, times(1)).publishUpdateProductEvent(any(ProductUpdateEvent.class));
        final Product product = productRepository.findById(savedProduct.getId()).get();
        assertThat(product.getId()).isEqualTo(savedProduct.getId());
        assertThat(product.getPriceValue()).isEqualTo(updatedPrice);
    }

    @Test
    @DisplayName("존재하지 않는 프로덕트의 정보를 업데이트 하는 경우 예외가 발생한다.")
    void updateFailByInvalidProductId() {
        //given
        final int updatedPrice = 1_000_000;
        final var productUpdateRequest = new ProductPriceUpdateRequest(updatedPrice);

        //when & then
        assertThatThrownBy(() -> updateProductUseCase.updateProductPrice(0L, productUpdateRequest))
                .isInstanceOf(NonExistentProductException.class)
                .hasMessageContaining("존재하지 않는 프로덕트입니다.");
    }

    @Test
    @DisplayName("프로덕트의 정보를 유효하지 않은 가격으로 업데이트 하는 경우 예외가 발생한다.")
    void updateFailByInvalidPrice() {
        //given
        final Product savedProduct = productRepository.save(Product.createWithoutId(10, 8L, 1L));

        final int updatedPrice = 1_000_001;
        final var productUpdateRequest = new ProductPriceUpdateRequest(updatedPrice);

        //when & then
        assertThatThrownBy(() -> updateProductUseCase.updateProductPrice(savedProduct.getId(), productUpdateRequest))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessageContaining("가격은 10원이상 1,000,000원 이하여야 합니다.");
    }
}
