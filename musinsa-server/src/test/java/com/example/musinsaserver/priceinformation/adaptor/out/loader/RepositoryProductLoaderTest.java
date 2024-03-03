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
}
