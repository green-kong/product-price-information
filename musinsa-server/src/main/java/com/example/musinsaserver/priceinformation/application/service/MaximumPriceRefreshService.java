package com.example.musinsaserver.priceinformation.application.service;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceRefreshUseCase;
import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.HighestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.ProductIsNotDeletedException;

@Component
@Transactional(readOnly = true)
public class MaximumPriceRefreshService implements MaximumPriceRefreshUseCase {

    private final HighestPriceInformationRepository highestPriceInformationRepository;
    private final ProductLoader productLoader;

    public MaximumPriceRefreshService(
            final HighestPriceInformationRepository highestPriceInformationRepository,
            final ProductLoader productLoader
    ) {
        this.highestPriceInformationRepository = highestPriceInformationRepository;
        this.productLoader = productLoader;
    }

    @Override
    @Transactional
    public void refreshMaximumPriceInformation(final Long productId) {
        validateProductId(productId);
        highestPriceInformationRepository.findByProductId(productId)
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
        final Long categoryId = currentMaximumPriceInformation.getCategoryId();
        final Optional<ProductLoadDto> productLoadDto = productLoader.loadHighestPriceProductByBrandIdAndCategory(
                brandId, categoryId);

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
            highestPriceInformationRepository.updateById(priceInformationId, currentMaximumPriceInformation);
        };
    }

    private Runnable delete(final Long priceInformationId) {
        return () -> highestPriceInformationRepository.deleteById(priceInformationId);
    }
}
