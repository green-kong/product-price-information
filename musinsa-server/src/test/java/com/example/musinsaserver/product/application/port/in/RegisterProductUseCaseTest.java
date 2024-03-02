package com.example.musinsaserver.product.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.musinsaserver.product.application.port.in.dto.RegisterProductRequest;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Category;
import com.example.musinsaserver.product.domain.Product;

@SpringBootTest
class RegisterProductUseCaseTest {

    @Autowired
    RegisterProductUseCase registerProductUseCase;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("product를 저장한다.")
    void registerProduct() {
        //given
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
}
