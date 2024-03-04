package com.example.musinsaserver.priceinformation.application.service;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceRefreshUseCase;
import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MaximumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.ProductIsNotDeletedException;

// TODO: 2024/03/04 아아... 중복코드.....
@Component
public class MaximumPriceRefreshService implements MaximumPriceRefreshUseCase {

    private final MaximumPriceInformationRepository maximumPriceInformationRepository;
    private final ProductLoader productLoader;

    public MaximumPriceRefreshService(
            final MaximumPriceInformationRepository maximumPriceInformationRepository,
            final ProductLoader productLoader
    ) {
        this.maximumPriceInformationRepository = maximumPriceInformationRepository;
        this.productLoader = productLoader;
    }

    @Override
    public void refreshMaximumPriceInformation(final Long productId) {
        validateProductId(productId);
        maximumPriceInformationRepository.findByProductId(productId)
                .ifPresent(this::refreshOrDelete);
    }

    private void validateProductId(final Long productId) {
        productLoader.loadProduct(productId)
                .ifPresent(productLoadDto -> {
                    throw new ProductIsNotDeletedException(productId);
                });
    }

    private void refreshOrDelete(final PriceInformation currentMaximumPriceInformation) {
        final Long priceInformationId = currentMaximumPriceInformation.getId();
        final Long brandId = currentMaximumPriceInformation.getBrandId();
        final String category = currentMaximumPriceInformation.getCategory();
        final Optional<ProductLoadDto> productLoadDto = productLoader.loadHighestPriceProductByBrandIdAndCategory(
                brandId, category);

        productLoadDto.ifPresentOrElse(
                refresh(currentMaximumPriceInformation, priceInformationId),
                delete(priceInformationId)
        );
    }

    private Consumer<ProductLoadDto> refresh(
            final PriceInformation currentMaximumPriceInformation,
            final Long priceInformationId
    ) {
        return foundProduct -> {
            currentMaximumPriceInformation.update(foundProduct.productId(), foundProduct.price());
            maximumPriceInformationRepository.updateById(priceInformationId, currentMaximumPriceInformation);
        };
    }

    private Runnable delete(final Long priceInformationId) {
        return () -> maximumPriceInformationRepository.deleteById(priceInformationId);
    }
}
