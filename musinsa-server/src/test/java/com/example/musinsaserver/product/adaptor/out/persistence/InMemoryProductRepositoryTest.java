package com.example.musinsaserver.product.adaptor.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.example.musinsaserver.product.domain.Product;

class InMemoryProductRepositoryTest {

    InMemoryProductRepository inMemoryProductRepository = new InMemoryProductRepository();

    @Test
    @DisplayName("product를 저장한다.")
    void save() {
        //given
        final int price = 1_000;
        final long brandId = 1L;
        final Long categoryId = 3L;
        final Product product = Product.createWithoutId(price, categoryId, brandId);

        //when
        final Product savedProduct = inMemoryProductRepository.save(product);

        //then
        assertSoftly(softAssertions -> {
            assertThat(savedProduct.getId()).isNotNull();
            assertThat(savedProduct.getPriceValue()).isEqualTo(price);
            assertThat(savedProduct.getBrandId()).isEqualTo(brandId);
            assertThat(savedProduct.getCategoryId()).isEqualTo(categoryId);
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
        final Product savedProduct = inMemoryProductRepository.save(product);

        //when
        final Product foundProduct = inMemoryProductRepository.findById(savedProduct.getId()).get();

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
        final Product savedProduct = inMemoryProductRepository.save(product);

        final int updatedPrice = 20_000;
        savedProduct.update(updatedPrice);

        //when
        inMemoryProductRepository.update(savedProduct);

        //then
        final Product updatedProduct = inMemoryProductRepository.findById(savedProduct.getId()).get();
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
        final Product savedProduct = inMemoryProductRepository.save(product);

        //when
        inMemoryProductRepository.delete(savedProduct.getId());

        //then
        final Optional<Product> foundProduct = inMemoryProductRepository.findById(savedProduct.getId());
        assertThat(foundProduct).isEmpty();
    }

    @Test
    @DisplayName("brandId와 category가 일치하는 product 중 가격이 가장 저렴한 product를 조회한다.")
    void findMinimumPriceProductByBrandIdAndCategory() {
        //given
        final int targetPrice = 1_000;
        final Long targetCategoryId = 3L;
        final long targetBrandId = 2L;
        final Product targetProduct = inMemoryProductRepository.save(
                Product.createWithoutId(targetPrice, targetCategoryId, targetBrandId));
        inMemoryProductRepository.save(Product.createWithoutId(2_000, targetCategoryId, 2L));
        inMemoryProductRepository.save(Product.createWithoutId(10_000, targetCategoryId, 2L));
        inMemoryProductRepository.save(Product.createWithoutId(1_00, 4L, 2L));
        inMemoryProductRepository.save(Product.createWithoutId(1_0, targetCategoryId, 5L));

        //when
        final Product found = inMemoryProductRepository.findMinimumPriceProductByBrandIdAndCategory(targetBrandId,
                targetCategoryId).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(found.getId()).isEqualTo(targetProduct.getId());
            assertThat(found.getBrandId()).isEqualTo(targetBrandId);
            assertThat(found.getCategoryId()).isEqualTo(targetCategoryId);
            assertThat(found.getPriceValue()).isEqualTo(targetPrice);
        });
    }

    @Test
    @DisplayName("brandId와 category가 일치하는 product가 없는 경우 Optional.empty를 반환한다.")
    void findMinimumPriceProductByBrandIdAndCategoryReturnEmpty() {
        //when
        final Optional<Product> found = inMemoryProductRepository.findMinimumPriceProductByBrandIdAndCategory(
                0L, 4L);

        //then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("brandId와 category가 일치하는 product 중 가격이 가장 비싼 product를 조회한다.")
    void findMaximumPriceProductByBrandIdAndCategory() {
        //given
        final int targetPrice = 1_000_000;
        final Long targetCategoryId = 3L;
        final long targetBrandId = 2L;
        final Product targetProduct = inMemoryProductRepository.save(
                Product.createWithoutId(targetPrice, targetCategoryId, targetBrandId));
        inMemoryProductRepository.save(Product.createWithoutId(2_000, targetCategoryId, 2L));
        inMemoryProductRepository.save(Product.createWithoutId(10_000, targetCategoryId, 2L));
        inMemoryProductRepository.save(Product.createWithoutId(1_00, 4L, 2L));
        inMemoryProductRepository.save(Product.createWithoutId(1_0, targetCategoryId, 5L));

        //when
        final Product found = inMemoryProductRepository.findMaximumPriceProductByBrandIdAndCategory(targetBrandId,
                targetCategoryId).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(found.getId()).isEqualTo(targetProduct.getId());
            assertThat(found.getBrandId()).isEqualTo(targetBrandId);
            assertThat(found.getCategoryId()).isEqualTo(targetCategoryId);
            assertThat(found.getPriceValue()).isEqualTo(targetPrice);
        });
    }

    @Test
    @DisplayName("brandId와 category가 일치하는 product가 없는 경우 Optional.empty를 반환한다.")
    void findMaximumPriceProductByBrandIdAndCategoryReturnEmpty() {
        //when
        final Optional<Product> found = inMemoryProductRepository.findMaximumPriceProductByBrandIdAndCategory(
                0L, 0L);

        //then
        assertThat(found).isEmpty();
    }
}
