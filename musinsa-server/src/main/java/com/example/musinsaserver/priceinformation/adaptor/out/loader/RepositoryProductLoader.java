package com.example.musinsaserver.priceinformation.adaptor.out.loader;

import static java.util.Objects.isNull;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.product.application.port.out.persistence.ProductRepository;
import com.example.musinsaserver.product.domain.Product;

@Component
public class RepositoryProductLoader implements ProductLoader {

    private final ProductRepository productRepository;

    public RepositoryProductLoader(final ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<ProductLoadDto> loadProduct(final Long id) {
        final Product product = productRepository.findById(id)
                .orElse(null);
        if (isNull(product)) {
            return Optional.empty();
        }
        return Optional.of(new ProductLoadDto(
                product.getId(),
                product.getBrandId(),
                product.getPriceValue(),
                product.getCategoryId()
        ));
    }

    @Override
    public Optional<ProductLoadDto> loadLowestPriceProductByBrandIdAndCategory(final Long brandId,
                                                                               final Long categoryId) {
        final Product product = productRepository.findMinimumPriceProductByBrandIdAndCategory(brandId, categoryId)
                .orElse(null);
        if (isNull(product)) {
            return Optional.empty();
        }
        return Optional.of(new ProductLoadDto(
                product.getId(),
                product.getBrandId(),
                product.getPriceValue(),
                product.getCategoryId()
        ));
    }

    @Override
    public Optional<ProductLoadDto> loadHighestPriceProductByBrandIdAndCategory(
            final Long brandId,
            final Long categoryId
    ) {
        final Product product = productRepository.findMaximumPriceProductByBrandIdAndCategory(brandId, categoryId)
                .orElse(null);
        if (isNull(product)) {
            return Optional.empty();
        }
        return Optional.of(new ProductLoadDto(
                product.getId(),
                product.getBrandId(),
                product.getPriceValue(),
                product.getCategoryId()
        ));
    }
}
