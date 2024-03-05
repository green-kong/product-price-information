package com.example.musinsaserver.priceinformation.application.service;

import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.priceinformation.application.port.in.MinimumPriceUpdateUseCase;
import com.example.musinsaserver.priceinformation.application.port.out.loader.BrandLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.CategoryLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.BrandLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidCategoryIdException;
import com.example.musinsaserver.priceinformation.exception.NonExistentProductWithBrandIdAndCategoryId;

@Service
@Transactional(readOnly = true)
public class MinimumPriceUpdateService implements MinimumPriceUpdateUseCase {

    private final LowestPriceInformationRepository lowestPriceInformationRepository;
    private final ProductLoader productLoader;
    private final BrandLoader brandLoader;
    private final CategoryLoader categoryLoader;

    public MinimumPriceUpdateService(
            final LowestPriceInformationRepository lowestPriceInformationRepository,
            final ProductLoader productLoader,
            final BrandLoader brandLoader,
            final CategoryLoader categoryLoader
    ) {
        this.lowestPriceInformationRepository = lowestPriceInformationRepository;
        this.productLoader = productLoader;
        this.brandLoader = brandLoader;
        this.categoryLoader = categoryLoader;
    }

    @Override
    @Transactional
    public void updateMinimumPrice(final Long brandId, final Long categoryId) {
        final ProductLoadDto productLoadDto = getLowestProductBy(brandId, categoryId);
        lowestPriceInformationRepository.findByBrandIdAndCategoryId(brandId, categoryId)
                .ifPresentOrElse(update(productLoadDto), save(productLoadDto));
    }

    private ProductLoadDto getLowestProductBy(final Long brandId, final Long categoryId) {
        return productLoader.loadLowestPriceProductByBrandIdAndCategory(brandId, categoryId)
                .orElseThrow(() -> new NonExistentProductWithBrandIdAndCategoryId(brandId, categoryId));
    }

    private Consumer<PriceInformation> update(final ProductLoadDto productLoadDto) {
        return currentMinimumPriceInformation -> {
            currentMinimumPriceInformation.update(productLoadDto.productId(), productLoadDto.price());
            lowestPriceInformationRepository.updateById(
                    currentMinimumPriceInformation.getId(),
                    currentMinimumPriceInformation
            );
        };
    }

    private Runnable save(final ProductLoadDto productLoadDto) {
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
            lowestPriceInformationRepository.save(priceInformation);
        };
    }
}
