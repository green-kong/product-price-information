package com.example.musinsaserver.product.application.port.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.musinsaserver.product.domain.Category;
import com.example.musinsaserver.product.domain.Product;

@SpringBootTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("product를 저장한다.")
    void save() {
        //given
        final int price = 1_000;
        final long brandId = 1L;
        final Product product = Product.createWithoutId(price, Category.BAG, brandId);

        //when
        final Product savedProduct = productRepository.save(product);

        //then
        assertSoftly(softAssertions -> {
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct.getPriceValue()).isEqualTo(price);
            assertThat(savedProduct.getCategory()).isEqualTo(Category.BAG);
            assertThat(savedProduct.getBrandId()).isEqualTo(brandId);
        });
    }

    @Test
    @DisplayName("저장된 product를 조회한다.")
    void findById() {
        //given
        final int price = 1_000;
        final long brandId = 1L;
        final Category category = Category.HAT;
        final Product product = Product.createWithoutId(price, category, brandId);
        final Product savedProduct = productRepository.save(product);

        //when
        final Product foundProduct = productRepository.findById(savedProduct.getId()).get();

        //then
        assertThat(foundProduct.getId()).isEqualTo(savedProduct.getId());
        assertThat(foundProduct.getPriceValue()).isEqualTo(price);
        assertThat(foundProduct.getBrandId()).isEqualTo(brandId);
        assertThat(foundProduct.getCategory()).isEqualTo(category);
    }


    @Test
    @DisplayName("프로덕트를 업데이트 한다.")
    void update() {
        //given
        final Product product = Product.createWithoutId(1_000, Category.PANTS, 1L);
        final Product savedProduct = productRepository.save(product);

        final int updatedPrice = 20_000;
        final Category category = Category.HAT;
        final long updatedBrandId = 3L;
        savedProduct.update(updatedPrice, "hat", updatedBrandId);

        //when
        productRepository.update(savedProduct);

        //then
        final Product updatedProduct = productRepository.findById(savedProduct.getId()).get();
        assertSoftly(softAssertions -> {
            assertThat(updatedProduct.getId()).isEqualTo(savedProduct.getId());
            assertThat(updatedProduct.getPriceValue()).isEqualTo(updatedPrice);
            assertThat(updatedProduct.getCategory()).isEqualTo(category);
            assertThat(updatedProduct.getBrandId()).isEqualTo(updatedBrandId);
        });
    }


    @Test
    @DisplayName("product를 삭제한다.")
    void deleteById() {
        //given
        final Product product = Product.createWithoutId(1_000, Category.PANTS, 1L);
        final Product savedProduct = productRepository.save(product);

        //when
        productRepository.delete(savedProduct.getId());

        //then
        final Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());
        assertThat(foundProduct).isEmpty();
    }
}
