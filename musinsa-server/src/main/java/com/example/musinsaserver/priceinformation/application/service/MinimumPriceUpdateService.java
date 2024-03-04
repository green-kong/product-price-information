package com.example.musinsaserver.priceinformation.application.service;

import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceUpdateUseCase;
import com.example.musinsaserver.priceinformation.application.port.out.loader.BrandLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.BrandLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MinimumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidProductIdException;

@Service
public class MinimumPriceUpdateService implements MinimumPriceUpdateUseCase {

    private final MinimumPriceInformationRepository minimumPriceInformationRepository;
    private final ProductLoader productLoader;
    private final BrandLoader brandLoader;

    public MinimumPriceUpdateService(
            final MinimumPriceInformationRepository minimumPriceInformationRepository,
            final ProductLoader productLoader,
            final BrandLoader brandLoader
    ) {
        this.minimumPriceInformationRepository = minimumPriceInformationRepository;
        this.productLoader = productLoader;
        this.brandLoader = brandLoader;
    }

    @Override
    public void updateMinimumPrice(final Long productId) {
        final ProductLoadDto productLoadDto = productLoader.loadProduct(productId)
                .orElseThrow(() -> new InvalidProductIdException(productId));
        minimumPriceInformationRepository.findByBrandIdAndCategory(
                productLoadDto.brandId(),
                productLoadDto.category()
        ).ifPresentOrElse(comparePriceAndUpdate(productLoadDto), saveRegisteredProductAsMinimum(productLoadDto));
    }

    private Consumer<PriceInformation> comparePriceAndUpdate(final ProductLoadDto productLoadDto) {
        return currentMinimumPriceInformation -> {
            if (currentMinimumPriceInformation.isMoreExpensiveThan(productLoadDto.price())) {
                updateMinimumPriceInformation(productLoadDto, currentMinimumPriceInformation);
            }
        };
    }

    private void updateMinimumPriceInformation(
            final ProductLoadDto productLoadDto,
            final PriceInformation currentMinimumPriceInformation
    ) {
        currentMinimumPriceInformation.update(productLoadDto.productId(), productLoadDto.price());
        minimumPriceInformationRepository.updateById(
                currentMinimumPriceInformation.getId(),
                currentMinimumPriceInformation
        );
    }

    private Runnable saveRegisteredProductAsMinimum(final ProductLoadDto productLoadDto) {
        return () -> {
            final BrandLoadDto brandLoadDto = brandLoader.loadBrand(productLoadDto.brandId())
                    .orElseThrow(() -> new InvalidBrandIdException(productLoadDto.brandId()));
            final PriceInformation priceInformation = PriceInformation.createWithoutId(
                    productLoadDto.productId(),
                    productLoadDto.brandId(),
                    productLoadDto.category(),
                    productLoadDto.price(),
                    brandLoadDto.brandName()
            );
            minimumPriceInformationRepository.save(priceInformation);
        };
    }
}
