package com.example.musinsaserver.priceinformation.adaptor.out.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.musinsaserver.product.adaptor.in.loader.RepositoryProductLoader;
import com.example.musinsaserver.common.loader.dto.ProductLoadDto;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.support.BaseTest;

class RepositoryProductLoaderTest extends BaseTest {

    @Autowired
    RepositoryProductLoader repositoryProductLoader;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("id와 일치하는 product를 불러온다.")
    void loadProduct() {
        //given
        final Product savedProduct = productRepository.save(Product.createWithoutId(20_000, 3L, 1L));

        //when
        final ProductLoadDto productLoadDto = repositoryProductLoader.loadProduct(savedProduct.getId()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(productLoadDto.productId()).isEqualTo(savedProduct.getId());
            assertThat(productLoadDto.brandId()).isEqualTo(savedProduct.getBrandId());
            assertThat(productLoadDto.price()).isEqualTo(savedProduct.getPriceValue());
            assertThat(productLoadDto.categoryId()).isEqualTo(savedProduct.getCategoryId());
        });
    }

    @Test
    @DisplayName("id와 일치하는 product가 없는 경우 Optional.empty를 반환한다.")
    void loadProductReturnOptionalEmpty() {
        //when
        final Optional<ProductLoadDto> productLoadDto = repositoryProductLoader.loadProduct(0L);

        //then
        assertThat(productLoadDto).isEmpty();
    }

    @Test
    @DisplayName("brandId와 categoryId가 일치하는 product 중 가장 저렴함 product의 정보를 불러온다.")
    void loadLowestPriceProductByBrandIdAndCategory() {
        //given
        final int targetPrice = 20_000;
        final Long targetCategoryId = 19L;
        final long targetBrandId = 1L;
        final Product targetProduct = productRepository.save(
                Product.createWithoutId(targetPrice, targetCategoryId, targetBrandId));

        productRepository.save(Product.createWithoutId(30_000, targetCategoryId, targetBrandId));
        productRepository.save(Product.createWithoutId(40_000, targetCategoryId, targetBrandId));
        productRepository.save(Product.createWithoutId(10_000, 2L, targetBrandId));
        productRepository.save(Product.createWithoutId(1_000, 1L, targetBrandId));
        productRepository.save(Product.createWithoutId(15_000, targetCategoryId, 3L));

        //when
        final ProductLoadDto productLoadDto = repositoryProductLoader.loadLowestPriceProductByBrandIdAndCategory(
                targetBrandId, targetCategoryId).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(productLoadDto.productId()).isEqualTo(targetProduct.getId());
            assertThat(productLoadDto.brandId()).isEqualTo(targetProduct.getBrandId());
            assertThat(productLoadDto.price()).isEqualTo(targetProduct.getPriceValue());
            assertThat(productLoadDto.categoryId()).isEqualTo(targetProduct.getCategoryId());
        });
    }


    @Test
    @DisplayName("brandId와 category가 일치하는 product가 없는 경우 Optional.empty를 반환한다.")
    void loadLowestPriceProductByBrandIdAndCategoryReturnEmpty() {
        //given
        final Long targetCategoryId = 0L;
        final long targetBrandId = 1L;
        productRepository.save(Product.createWithoutId(10_000, 1L, targetBrandId));
        productRepository.save(Product.createWithoutId(1_000, 2L, targetBrandId));
        productRepository.save(Product.createWithoutId(15_000, targetCategoryId, 3L));

        //when
        final Optional<ProductLoadDto> productLoadDto = repositoryProductLoader.loadLowestPriceProductByBrandIdAndCategory(
                targetBrandId, targetCategoryId);

        //then
        assertThat(productLoadDto).isEmpty();
    }

    @Test
    @DisplayName("brandId와 category가 일치하는 product 중 가장 비싼 product의 정보를 불러온다.")
    void loadHighestPriceProductByBrandIdAndCategory() {
        //given
        final int targetPrice = 1_000_000;
        final Long targetCategoryId = 3L;
        final long targetBrandId = 30L;
        final Product targetProduct = productRepository.save(
                Product.createWithoutId(targetPrice, targetCategoryId, targetBrandId));

        productRepository.save(Product.createWithoutId(30_000, targetCategoryId, targetBrandId));
        productRepository.save(Product.createWithoutId(40_000, targetCategoryId, targetBrandId));
        productRepository.save(Product.createWithoutId(10_000, 2L, targetBrandId));
        productRepository.save(Product.createWithoutId(1_000, 1L, targetBrandId));
        productRepository.save(Product.createWithoutId(15_000, targetCategoryId, 3L));

        //when
        final ProductLoadDto productLoadDto = repositoryProductLoader.loadHighestPriceProductByBrandIdAndCategory(
                targetBrandId, targetCategoryId).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(productLoadDto.productId()).isEqualTo(targetProduct.getId());
            assertThat(productLoadDto.brandId()).isEqualTo(targetProduct.getBrandId());
            assertThat(productLoadDto.price()).isEqualTo(targetProduct.getPriceValue());
            assertThat(productLoadDto.categoryId()).isEqualTo(targetProduct.getCategoryId());
        });
    }


    @Test
    @DisplayName("brandId와 category가 일치하는 product가 없는 경우 Optional.empty를 반환한다.")
    void loadHighestPriceProductByBrandIdAndCategoryReturnEmpty() {
        //given
        final Long targetCategoryId = 3L;
        final long targetBrandId = 31L;
        productRepository.save(Product.createWithoutId(10_000, 1L, targetBrandId));
        productRepository.save(Product.createWithoutId(1_000, 2L, targetBrandId));
        productRepository.save(Product.createWithoutId(15_000, targetCategoryId, 3L));

        //when
        final Optional<ProductLoadDto> productLoadDto = repositoryProductLoader.loadHighestPriceProductByBrandIdAndCategory(
                targetBrandId, targetCategoryId);

        //then
        assertThat(productLoadDto).isEmpty();
    }
}
