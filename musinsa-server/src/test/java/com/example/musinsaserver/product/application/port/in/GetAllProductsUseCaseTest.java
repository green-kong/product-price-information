package com.example.musinsaserver.product.application.port.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.musinsaserver.common.loader.BrandLoader;
import com.example.musinsaserver.common.loader.CategoryLoader;
import com.example.musinsaserver.common.loader.dto.BrandLoadDto;
import com.example.musinsaserver.common.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.product.application.port.in.dto.ProductResponse;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.CantFindBrandException;
import com.example.musinsaserver.product.exception.CantFindCategoryException;
import com.example.musinsaserver.support.BaseTest;

class GetAllProductsUseCaseTest extends BaseTest {

    @Autowired
    GetAllProductsUseCase useCase;

    @Autowired
    ProductRepository productRepository;

    @MockBean
    BrandLoader brandLoader;

    @MockBean
    CategoryLoader categoryLoader;

    @Test
    @DisplayName("모든 프로덕트 정보를 반환한다.")
    void getAllProducts() {
        //given
        final CategoryLoadDto pants = new CategoryLoadDto(6L, "바지");
        final CategoryLoadDto outer = new CategoryLoadDto(7L, "아우터");
        final CategoryLoadDto accessories = new CategoryLoadDto(8L, "액세서리");
        final CategoryLoadDto hat = new CategoryLoadDto(9L, "모자");
        final CategoryLoadDto bag = new CategoryLoadDto(10L, "가방");
        when(categoryLoader.loadCategoriesByIds(anyList())).thenReturn(List.of(pants, outer, accessories, hat, bag));

        final BrandLoadDto brandA = new BrandLoadDto(1L, "brandA");
        final BrandLoadDto brandB = new BrandLoadDto(2L, "brandB");
        final BrandLoadDto brandC = new BrandLoadDto(3L, "brandC");
        final BrandLoadDto brandD = new BrandLoadDto(4L, "brandD");
        final BrandLoadDto brandE = new BrandLoadDto(5L, "brandE");
        when(brandLoader.loadBrandByIds(anyList())).thenReturn(List.of(brandA, brandB, brandC, brandD, brandE));

        final Product product1 = productRepository.save(Product.createWithoutId(10000, 6L, 1L));
        final Product product2 = productRepository.save(Product.createWithoutId(20000, 7L, 2L));
        final Product product3 = productRepository.save(Product.createWithoutId(30000, 8L, 3L));
        final Product product4 = productRepository.save(Product.createWithoutId(40000, 9L, 4L));
        final Product product5 = productRepository.save(Product.createWithoutId(50000, 10L, 5L));

        //when
        final List<ProductResponse> products = useCase.getAllProducts();

        //then
        final List<Long> targetIds = Stream.of(product1, product2, product3, product4, product5)
                .map(Product::getId)
                .toList();
        final List<Long> foundIds = products.stream()
                .map(ProductResponse::id)
                .toList();
        assertSoftly(softAssertions -> {
            assertThat(products).hasSize(5);
            assertThat(foundIds).containsExactlyInAnyOrderElementsOf(targetIds);
        });
    }

    @Test
    @DisplayName("프로덕트의 브랜드 id와 일치하는 브랜드가 존재하지 않는경우 예외가 발생한다.")
    void getAllProductsFailByBrand() {
        //given
        final CategoryLoadDto pants = new CategoryLoadDto(6L, "바지");
        final CategoryLoadDto outer = new CategoryLoadDto(7L, "아우터");
        final CategoryLoadDto accessories = new CategoryLoadDto(8L, "액세서리");
        final CategoryLoadDto hat = new CategoryLoadDto(9L, "모자");
        final CategoryLoadDto bag = new CategoryLoadDto(10L, "가방");
        when(categoryLoader.loadCategoriesByIds(anyList())).thenReturn(List.of(pants, outer, accessories, hat, bag));

        final BrandLoadDto brandA = new BrandLoadDto(1L, "brandA");
        final BrandLoadDto brandB = new BrandLoadDto(2L, "brandB");
        final BrandLoadDto brandC = new BrandLoadDto(3L, "brandC");
        final BrandLoadDto brandD = new BrandLoadDto(4L, "brandD");
        when(brandLoader.loadBrandByIds(anyList())).thenReturn(List.of(brandA, brandB, brandC, brandD));

        productRepository.save(Product.createWithoutId(10000, 6L, 1L));
        productRepository.save(Product.createWithoutId(20000, 7L, 2L));
        productRepository.save(Product.createWithoutId(30000, 8L, 3L));
        productRepository.save(Product.createWithoutId(40000, 9L, 4L));
        productRepository.save(Product.createWithoutId(50000, 10L, 5L));

        //when & then
        assertThatThrownBy(() -> useCase.getAllProducts())
                .isInstanceOf(CantFindBrandException.class)
                .hasMessageContaining("저장 된 product의 brand를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("프로덕트의 카테고리 id와 일치하는 카테고리가 존재하지 않는경우 예외가 발생한다.")
    void getAllProductsFailByCategory() {
        //given
        final CategoryLoadDto pants = new CategoryLoadDto(6L, "바지");
        final CategoryLoadDto outer = new CategoryLoadDto(7L, "아우터");
        final CategoryLoadDto accessories = new CategoryLoadDto(8L, "액세서리");
        final CategoryLoadDto hat = new CategoryLoadDto(9L, "모자");
        when(categoryLoader.loadCategoriesByIds(anyList())).thenReturn(List.of(pants, outer, accessories, hat));

        final BrandLoadDto brandA = new BrandLoadDto(1L, "brandA");
        final BrandLoadDto brandB = new BrandLoadDto(2L, "brandB");
        final BrandLoadDto brandC = new BrandLoadDto(3L, "brandC");
        final BrandLoadDto brandD = new BrandLoadDto(4L, "brandD");
        final BrandLoadDto brandE = new BrandLoadDto(5L, "brandE");
        when(brandLoader.loadBrandByIds(anyList())).thenReturn(List.of(brandA, brandB, brandC, brandD, brandE));

        productRepository.save(Product.createWithoutId(10000, 6L, 1L));
        productRepository.save(Product.createWithoutId(20000, 7L, 2L));
        productRepository.save(Product.createWithoutId(30000, 8L, 3L));
        productRepository.save(Product.createWithoutId(40000, 9L, 4L));
        productRepository.save(Product.createWithoutId(50000, 10L, 5L));

        //when & then
        assertThatThrownBy(() -> useCase.getAllProducts())
                .isInstanceOf(CantFindCategoryException.class)
                .hasMessageContaining("저장 된 product의 category를 찾을 수 없습니다.");
    }
}
