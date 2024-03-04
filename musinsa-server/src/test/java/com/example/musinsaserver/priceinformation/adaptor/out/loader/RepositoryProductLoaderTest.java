package com.example.musinsaserver.priceinformation.adaptor.out.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Category;
import com.example.musinsaserver.product.domain.Product;

@SpringBootTest
class RepositoryProductLoaderTest {

    @Autowired
    RepositoryProductLoader repositoryProductLoader;

    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("id와 일치하는 product를 불러온다.")
    void loadProduct() {
        //given
        final Product savedProduct = productRepository.save(Product.createWithoutId(20_000, Category.ACCESSORIES, 1L));

        //when
        final ProductLoadDto productLoadDto = repositoryProductLoader.loadProduct(savedProduct.getId()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(productLoadDto.productId()).isEqualTo(savedProduct.getId());
            assertThat(productLoadDto.brandId()).isEqualTo(savedProduct.getBrandId());
            assertThat(productLoadDto.price()).isEqualTo(savedProduct.getPriceValue());
            assertThat(productLoadDto.category()).isEqualTo(savedProduct.getCategory().getValue());
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
    @DisplayName("brandId와 category가 일치하는 product 중 가장 저렴함 product의 정보를 불러온다.")
    void loadLowestPriceProductByBrandIdAndCategory() {
        //given
        final int targetPrice = 20_000;
        final Category targetCategory = Category.TOP;
        final long targetBrandId = 1L;
        final Product targetProduct = productRepository.save(
                Product.createWithoutId(targetPrice, targetCategory, targetBrandId));

        productRepository.save(Product.createWithoutId(30_000, targetCategory, targetBrandId));
        productRepository.save(Product.createWithoutId(40_000, targetCategory, targetBrandId));
        productRepository.save(Product.createWithoutId(10_000, Category.PANTS, targetBrandId));
        productRepository.save(Product.createWithoutId(1_000, Category.SNEAKERS, targetBrandId));
        productRepository.save(Product.createWithoutId(15_000, targetCategory, 3L));

        //when
        final ProductLoadDto productLoadDto = repositoryProductLoader.loadLowestPriceProductByBrandIdAndCategory(
                targetBrandId, targetCategory.getValue()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(productLoadDto.productId()).isEqualTo(targetProduct.getId());
            assertThat(productLoadDto.brandId()).isEqualTo(targetProduct.getBrandId());
            assertThat(productLoadDto.price()).isEqualTo(targetProduct.getPriceValue());
            assertThat(productLoadDto.category()).isEqualTo(targetProduct.getCategory().getValue());
        });
    }


    @Test
    @DisplayName("brandId와 category가 일치하는 product가 없는 경우 Optional.empty를 반환한다.")
    void loadLowestPriceProductByBrandIdAndCategoryReturnEmpty() {
        //given
        final Category targetCategory = Category.SOCKS;
        final long targetBrandId = 1L;
        productRepository.save(Product.createWithoutId(10_000, Category.PANTS, targetBrandId));
        productRepository.save(Product.createWithoutId(1_000, Category.SNEAKERS, targetBrandId));
        productRepository.save(Product.createWithoutId(15_000, targetCategory, 3L));

        //when
        final Optional<ProductLoadDto> productLoadDto = repositoryProductLoader.loadLowestPriceProductByBrandIdAndCategory(
                targetBrandId, targetCategory.getValue());

        //then
        assertThat(productLoadDto).isEmpty();
    }

    @Test
    @DisplayName("brandId와 category가 일치하는 product 중 가장 비싼 product의 정보를 불러온다.")
    void loadHighestPriceProductByBrandIdAndCategory() {
        //given
        final int targetPrice = 1_000_000;
        final Category targetCategory = Category.TOP;
        final long targetBrandId = 30L;
        final Product targetProduct = productRepository.save(
                Product.createWithoutId(targetPrice, targetCategory, targetBrandId));

        productRepository.save(Product.createWithoutId(30_000, targetCategory, targetBrandId));
        productRepository.save(Product.createWithoutId(40_000, targetCategory, targetBrandId));
        productRepository.save(Product.createWithoutId(10_000, Category.PANTS, targetBrandId));
        productRepository.save(Product.createWithoutId(1_000, Category.SNEAKERS, targetBrandId));
        productRepository.save(Product.createWithoutId(15_000, targetCategory, 3L));

        //when
        final ProductLoadDto productLoadDto = repositoryProductLoader.loadHighestPriceProductByBrandIdAndCategory(
                targetBrandId, targetCategory.getValue()).get();

        //then
        assertSoftly(softAssertions -> {
            assertThat(productLoadDto.productId()).isEqualTo(targetProduct.getId());
            assertThat(productLoadDto.brandId()).isEqualTo(targetProduct.getBrandId());
            assertThat(productLoadDto.price()).isEqualTo(targetProduct.getPriceValue());
            assertThat(productLoadDto.category()).isEqualTo(targetProduct.getCategory().getValue());
        });
    }


    @Test
    @DisplayName("brandId와 category가 일치하는 product가 없는 경우 Optional.empty를 반환한다.")
    void loadHighestPriceProductByBrandIdAndCategoryReturnEmpty() {
        //given
        final Category targetCategory = Category.SOCKS;
        final long targetBrandId = 31L;
        productRepository.save(Product.createWithoutId(10_000, Category.PANTS, targetBrandId));
        productRepository.save(Product.createWithoutId(1_000, Category.SNEAKERS, targetBrandId));
        productRepository.save(Product.createWithoutId(15_000, targetCategory, 3L));

        //when
        final Optional<ProductLoadDto> productLoadDto = repositoryProductLoader.loadHighestPriceProductByBrandIdAndCategory(
                targetBrandId, targetCategory.getValue());

        //then
        assertThat(productLoadDto).isEmpty();
    }
}
