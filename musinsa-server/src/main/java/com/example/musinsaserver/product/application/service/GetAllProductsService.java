package com.example.musinsaserver.product.application.service;

import static java.util.Objects.isNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.common.loader.BrandLoader;
import com.example.musinsaserver.common.loader.CategoryLoader;
import com.example.musinsaserver.common.loader.dto.BrandLoadDto;
import com.example.musinsaserver.common.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.product.application.port.in.GetAllProductsUseCase;
import com.example.musinsaserver.product.application.port.in.dto.ProductResponse;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;
import com.example.musinsaserver.product.exception.CantFindBrandException;
import com.example.musinsaserver.product.exception.CantFindCategoryException;

@Service
@Transactional(readOnly = true)
public class GetAllProductsService implements GetAllProductsUseCase {
    private final BrandLoader brandLoader;
    private final CategoryLoader categoryLoader;
    private final ProductRepository productRepository;

    public GetAllProductsService(
            final BrandLoader brandLoader,
            final CategoryLoader categoryLoader,
            final ProductRepository productRepository
    ) {
        this.brandLoader = brandLoader;
        this.categoryLoader = categoryLoader;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        final List<Product> products = productRepository.findAll();
        final Map<Long, String> brands = getBrands(products);
        final Map<Long, String> categories = getCategories(products);
        return products.stream()
                .map(product -> getProductResponse(brands, categories, product))
                .toList();
    }

    private Map<Long, String> getBrands(final List<Product> products) {
        final List<Long> brandIds = products.stream()
                .map(Product::getBrandId)
                .distinct()
                .toList();
        return brandLoader.loadBrandByIds(brandIds)
                .stream()
                .collect(Collectors.toMap(BrandLoadDto::brandId, BrandLoadDto::brandName));
    }

    private Map<Long, String> getCategories(final List<Product> products) {
        final List<Long> categoryIds = products.stream()
                .map(Product::getCategoryId)
                .distinct()
                .toList();
        return categoryLoader.loadCategoriesByIds(categoryIds)
                .stream()
                .collect(Collectors.toMap(CategoryLoadDto::id, CategoryLoadDto::category));
    }

    private ProductResponse getProductResponse(
            final Map<Long, String> brands,
            final Map<Long, String> categories,
            final Product product
    ) {
        final String brand = getBrandNameOrThrow(brands, product);
        final String category = getCategoryNameOrThrow(categories, product);
        return ProductResponse.of(product, brand, category);
    }

    private String getCategoryNameOrThrow(final Map<Long, String> categories, final Product product) {
        final String category = categories.get(product.getCategoryId());
        if (isNull(category)) {
            throw new CantFindCategoryException(product.getId(), product.getCategoryId());
        }
        return category;
    }

    private String getBrandNameOrThrow(final Map<Long, String> brands, final Product product) {
        final String brand = brands.get(product.getBrandId());
        if (isNull(brand)) {
            throw new CantFindBrandException(product.getId(), product.getBrandId());
        }
        return brand;
    }
}
