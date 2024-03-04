package com.example.musinsaserver.product.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.NonExistentProductException;
import com.example.musinsaserver.support.BaseTest;

class DeleteProductUseCaseTest extends BaseTest {

    @Autowired
    DeleteProductUseCase deleteProductUseCase;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("저장된 프로덕트를 삭제한다.")
    void deleteProduct() {
        //given
        final Product savedProduct = productRepository.save(Product.createWithoutId(10_000, 1L, 1L));

        //when
        deleteProductUseCase.delete(savedProduct.getId());

        //then
        final Optional<Product> product = productRepository.findById(savedProduct.getId());
        assertThat(product).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 프로덕트Id를 통해 삭제하는 경우 예외가 발생한다.")
    void deleteProductFailByInvalidProductId() {
        //when & then
        assertThatThrownBy(() -> deleteProductUseCase.delete(0L))
                .isInstanceOf(NonExistentProductException.class)
                .hasMessageContaining("존재하지 않는 프로덕트입니다.");

    }
}
