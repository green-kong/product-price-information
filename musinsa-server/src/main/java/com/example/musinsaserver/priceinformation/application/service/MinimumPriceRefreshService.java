package com.example.musinsaserver.priceinformation.application.service;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceRefreshUseCase;
import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MinimumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.ProductIsNotDeletedException;

@Component
public class MinimumPriceRefreshService implements MinimumPriceRefreshUseCase {

    private final MinimumPriceInformationRepository minimumPriceInformationRepository;
    private final ProductLoader productLoader;

    public MinimumPriceRefreshService(final MinimumPriceInformationRepository minimumPriceInformationRepository,
                                      final ProductLoader productLoader) {
        this.minimumPriceInformationRepository = minimumPriceInformationRepository;
        this.productLoader = productLoader;
    }

    @Override
    public void refreshMinimumPriceInformation(final Long productId) {
        validateProductId(productId);
        minimumPriceInformationRepository.findByProductId(productId)
                .ifPresent(this::refreshOrDelete);
    }

    private void validateProductId(final Long productId) {
        productLoader.loadProduct(productId)
                .ifPresent(productLoadDto -> {
                    throw new ProductIsNotDeletedException(productId);
                });
    }

    private void refreshOrDelete(final PriceInformation currentMinimumPriceInformation) {
        final Long priceInformationId = currentMinimumPriceInformation.getId();
        final Long brandId = currentMinimumPriceInformation.getBrandId();
        final String category = currentMinimumPriceInformation.getCategory();
        final Optional<ProductLoadDto> productLoadDto = productLoader.loadLowestPriceProductByBrandIdAndCategory(
                brandId, category);

        productLoadDto.ifPresentOrElse(
                refresh(currentMinimumPriceInformation, priceInformationId),
                delete(priceInformationId)
        );
    }

    private Consumer<ProductLoadDto> refresh(
            final PriceInformation currentMinimumPriceInformation,
            final Long priceInformationId
    ) {
        return foundProduct -> {
            currentMinimumPriceInformation.update(foundProduct.productId(), foundProduct.price());
            minimumPriceInformationRepository.updateById(priceInformationId, currentMinimumPriceInformation);
        };
    }

    private Runnable delete(final Long priceInformationId) {
        return () -> minimumPriceInformationRepository.deleteById(priceInformationId);
    }
}
