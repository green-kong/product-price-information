package com.example.musinsaserver.priceinformation.application.service;

import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceUpdateUseCase;
import com.example.musinsaserver.priceinformation.application.port.out.loader.BrandLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.CategoryLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.BrandLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MinimumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidCategoryIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidProductIdException;

@Service
public class MinimumPriceUpdateService implements MinimumPriceUpdateUseCase {

    private final MinimumPriceInformationRepository minimumPriceInformationRepository;
    private final ProductLoader productLoader;
    private final BrandLoader brandLoader;
    private final CategoryLoader categoryLoader;

    public MinimumPriceUpdateService(
            final MinimumPriceInformationRepository minimumPriceInformationRepository,
            final ProductLoader productLoader,
            final BrandLoader brandLoader,
            final CategoryLoader categoryLoader
    ) {
        this.minimumPriceInformationRepository = minimumPriceInformationRepository;
        this.productLoader = productLoader;
        this.brandLoader = brandLoader;
        this.categoryLoader = categoryLoader;
    }

    @Override
    public void updateMinimumPrice(final Long productId) {
        final ProductLoadDto productLoadDto = productLoader.loadProduct(productId)
                .orElseThrow(() -> new InvalidProductIdException(productId));
        minimumPriceInformationRepository.findByBrandIdAndCategoryId(
                productLoadDto.brandId(),
                productLoadDto.categoryId()
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
            final CategoryLoadDto categoryLoadDto = categoryLoader.loadCategory(productLoadDto.categoryId())
                    .orElseThrow(() -> new InvalidCategoryIdException(productLoadDto.productId()));

            final BrandLoadDto brandLoadDto = brandLoader.loadBrand(productLoadDto.brandId())
                    .orElseThrow(() -> new InvalidBrandIdException(productLoadDto.brandId()));

            final PriceInformation priceInformation = PriceInformation.createWithoutId(
                    productLoadDto.productId(),
                    productLoadDto.categoryId(),
                    productLoadDto.brandId(),
                    categoryLoadDto.category(),
                    productLoadDto.price(),
                    brandLoadDto.brandName()
            );
            minimumPriceInformationRepository.save(priceInformation);
        };
    }
}
