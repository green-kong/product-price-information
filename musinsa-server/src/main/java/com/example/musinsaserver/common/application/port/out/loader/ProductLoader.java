package com.example.musinsaserver.common.application.port.out.loader;

import java.util.Optional;

import com.example.musinsaserver.common.application.port.out.loader.dto.ProductLoadDto;

public interface ProductLoader {

    Optional<ProductLoadDto> loadProduct(final Long id);

    Optional<ProductLoadDto> loadLowestPriceProductByBrandIdAndCategory(final Long brandId, final Long categoryId);

    Optional<ProductLoadDto> loadHighestPriceProductByBrandIdAndCategory(final Long brandId, final Long categoryId);
}
