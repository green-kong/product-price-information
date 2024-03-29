package com.example.musinsaserver.product.application.port.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.support.BaseTest;

class ProductRepositoryTest extends BaseTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("product를 저장한다.")
    void save() {
        //given
        final int price = 1_000;
        final long brandId = 1L;
        final long categoryId = 1L;
        final Product product = Product.createWithoutId(price, categoryId, brandId);

        //when
        final Product savedProduct = productRepository.save(product);

        //then
        assertSoftly(softAssertions -> {
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct.getPriceValue()).isEqualTo(price);
            assertThat(savedProduct.getCategoryId()).isEqualTo(categoryId);
            assertThat(savedProduct.getBrandId()).isEqualTo(brandId);
        });
    }

    @Test
    @DisplayName("저장된 product를 조회한다.")
    void findById() {
        //given
        final int price = 1_000;
        final long brandId = 1L;
        final Long categoryId = 3L;
        final Product product = Product.createWithoutId(price, categoryId, brandId);
        final Product savedProduct = productRepository.save(product);

        //when
        final Product foundProduct = productRepository.findById(savedProduct.getId()).get();

        //then
        assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());
        assertThat(foundProduct.getPriceValue()).isEqualTo(price);
        assertThat(foundProduct.getBrandId()).isEqualTo(brandId);
        assertThat(foundProduct.getCategoryId()).isEqualTo(categoryId);
    }


    @Test
    @DisplayName("프로덕트를 업데이트 한다.")
    void update() {
        //given
        final Product product = Product.createWithoutId(1_000, 3L, 1L);
        final Product savedProduct = productRepository.save(product);

        final int updatedPrice = 20_000;
        savedProduct.update(updatedPrice);

        //when
        productRepository.update(savedProduct);

        //then
        final Product updatedProduct = productRepository.findById(savedProduct.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(updatedProduct.getId()).isEqualTo(savedProduct.getId());
            assertThat(updatedProduct.getPriceValue()).isEqualTo(updatedPrice);
        });
    }


    @Test
    @DisplayName("product를 삭제한다.")
    void deleteById() {
        //given
        final Product product = Product.createWithoutId(1_000, 3L, 1L);
        final Product savedProduct = productRepository.save(product);

        //when
        productRepository.delete(savedProduct.getId());

        //then
        final Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        assertThat(foundProduct).isEmpty();
    }

    @Test
    @DisplayName("모든 product를 조회한다.")
    void findAll() {
        //given
        productRepository.save(Product.createWithoutId(1_000, 3L, 1L));
        productRepository.save(Product.createWithoutId(2_000, 5L, 2L));
        productRepository.save(Product.createWithoutId(3_000, 6L, 3L));
        productRepository.save(Product.createWithoutId(4_000, 1L, 5L));

        //when
        final List<Product> products = productRepository.findAll();

        //then
        assertThat(products).hasSize(4);
    }
}
