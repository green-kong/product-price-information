package com.example.musinsaserver.product.adaptor.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.musinsaserver.product.domain.Category;
import com.example.musinsaserver.product.domain.Product;

class InMemoryProductRepositoryTest {

    InMemoryProductRepository inMemoryProductRepository = new InMemoryProductRepository();

    @Test
    @DisplayName("product를 저장한다.")
    void save() {
        //given
        final int price = 1_000;
        final long brandId = 1L;
        final Category category = Category.PANTS;
        final Product product = Product.createWithoutId(price, category, brandId);

        //when
        final Product savedProduct = inMemoryProductRepository.save(product);

        //then
        assertSoftly(softAssertions -> {
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct.getPriceValue()).isEqualTo(price);
            assertThat(savedProduct.getBrandId()).isEqualTo(brandId);
            assertThat(savedProduct.getCategory()).isEqualTo(category);
        });
    }

    @Test
    @DisplayName("저장된 product를 조회한다.")
    void findById() {
        //given
        final int price = 1_000;
        final long brandId = 1L;
        final Category category = Category.SNEAKERS;
        final Product product = Product.createWithoutId(price, category,brandId);
        final Product savedProduct = inMemoryProductRepository.save(product);

        //when
        final Product foundProduct = inMemoryProductRepository.findById(savedProduct.getId()).get();

        //then
        assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());
        assertThat(foundProduct.getPriceValue()).isEqualTo(price);
        assertThat(foundProduct.getBrandId()).isEqualTo(brandId);
        assertThat(foundProduct.getCategory()).isEqualTo(category);
    }
}
