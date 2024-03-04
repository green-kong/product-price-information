package com.example.musinsaserver.product.adaptor.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

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
        final Product product = Product.createWithoutId(price, category, brandId);
        final Product savedProduct = inMemoryProductRepository.save(product);

        //when
        final Product foundProduct = inMemoryProductRepository.findById(savedProduct.getId()).get();

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
        final Product savedProduct = inMemoryProductRepository.save(product);

        final int updatedPrice = 20_000;
        final Category category = Category.HAT;
        final long updatedBrandId = 3L;
        savedProduct.update(updatedPrice, "hat", updatedBrandId);

        //when
        inMemoryProductRepository.update(savedProduct);

        //then
        final Product updatedProduct = inMemoryProductRepository.findById(savedProduct.getId()).get();
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
        final Category targetCategory = Category.PANTS;
        final long targetBrandId = 2L;
        final Product targetProduct = inMemoryProductRepository.save(
                Product.createWithoutId(targetPrice, targetCategory, targetBrandId));
        inMemoryProductRepository.save(Product.createWithoutId(2_000, targetCategory, 2L));
        inMemoryProductRepository.save(Product.createWithoutId(10_000, targetCategory, 2L));
        inMemoryProductRepository.save(Product.createWithoutId(1_00, Category.ACCESSORIES, 2L));
        inMemoryProductRepository.save(Product.createWithoutId(1_0, targetCategory, 5L));

        //when
        final Product found = inMemoryProductRepository.findMinimumPriceProductByBrandIdAndCategory(targetBrandId,
                targetCategory.getValue()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(found.getId()).isEqualTo(targetProduct.getId());
            assertThat(found.getBrandId()).isEqualTo(targetBrandId);
            assertThat(found.getCategory()).isEqualTo(targetCategory);
            assertThat(found.getPriceValue()).isEqualTo(targetPrice);
        });
    }

    @Test
    @DisplayName("brandId와 category가 일치하는 product가 없는 경우 Optional.empty를 반환한다.")
    void findMinimumPriceProductByBrandIdAndCategoryReturnEmpty() {
        //when
        final Optional<Product> found = inMemoryProductRepository.findMinimumPriceProductByBrandIdAndCategory(
                0L, Category.SOCKS.getValue());

        //then
        assertThat(found).isEmpty();
    }
}
