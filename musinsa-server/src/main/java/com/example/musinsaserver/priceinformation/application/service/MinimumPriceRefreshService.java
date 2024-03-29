package com.example.musinsaserver.priceinformation.application.service;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.common.loader.ProductLoader;
import com.example.musinsaserver.common.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceRefreshUseCase;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.ProductIsNotDeletedException;

@Service
@Transactional
public class MinimumPriceRefreshService implements MinimumPriceRefreshUseCase {

    private final LowestPriceInformationRepository lowestPriceInformationRepository;
    private final ProductLoader productLoader;

    public MinimumPriceRefreshService(final LowestPriceInformationRepository lowestPriceInformationRepository,
                                      final ProductLoader productLoader) {
        this.lowestPriceInformationRepository = lowestPriceInformationRepository;
        this.productLoader = productLoader;
    }

    @Override
    public void refreshMinimumPriceInformation(final Long productId) {
        validateProductId(productId);
        lowestPriceInformationRepository.findByProductId(productId)
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
        final Long categoryId = currentMinimumPriceInformation.getCategoryId();
        final Optional<ProductLoadDto> productLoadDto = productLoader.loadLowestPriceProductByBrandIdAndCategory(
                brandId, categoryId);

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
            lowestPriceInformationRepository.updateById(priceInformationId, currentMinimumPriceInformation);
        };
    }

    private Runnable delete(final Long priceInformationId) {
        return () -> lowestPriceInformationRepository.deleteById(priceInformationId);
    }
}
