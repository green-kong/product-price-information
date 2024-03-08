package com.example.musinsaserver.priceinformation.application.service;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceUpdateUseCase;
import com.example.musinsaserver.common.loader.BrandLoader;
import com.example.musinsaserver.common.loader.CategoryLoader;
import com.example.musinsaserver.common.loader.ProductLoader;
import com.example.musinsaserver.common.loader.dto.BrandLoadDto;
import com.example.musinsaserver.common.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.common.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.HighestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidCategoryIdException;
import com.example.musinsaserver.priceinformation.exception.NonExistentProductWithBrandIdAndCategoryId;

@Service
@Transactional(readOnly = true)
public class MaximumPriceUpdateService implements MaximumPriceUpdateUseCase {

    private final HighestPriceInformationRepository highestPriceInformationRepository;
    private final ProductLoader productLoader;
    private final BrandLoader brandLoader;
    private final CategoryLoader categoryLoader;

    public MaximumPriceUpdateService(
            final HighestPriceInformationRepository highestPriceInformationRepository,
            final ProductLoader productLoader,
            final BrandLoader brandLoader,
            final CategoryLoader categoryLoader
    ) {
        this.highestPriceInformationRepository = highestPriceInformationRepository;
        this.productLoader = productLoader;
        this.brandLoader = brandLoader;
        this.categoryLoader = categoryLoader;
    }

    @Override
    @Transactional(propagation = REQUIRES_NEW)
    public void updateMaximumPrice(final Long brandId, final Long categoryId) {
        final ProductLoadDto productLoadDto = getHighestProductBy(brandId, categoryId);
         highestPriceInformationRepository.findByBrandIdAndCategoryId(
                productLoadDto.brandId(),
                productLoadDto.categoryId()
        ).ifPresentOrElse(update(productLoadDto), save(productLoadDto));
    }

    private ProductLoadDto getHighestProductBy(final Long brandId, final Long categoryId) {
        return productLoader.loadHighestPriceProductByBrandIdAndCategory(brandId, categoryId)
                .orElseThrow(() -> new NonExistentProductWithBrandIdAndCategoryId(brandId, categoryId));
    }

    private Consumer<PriceInformation> update(final ProductLoadDto productLoadDto) {
        return currentMaximumPriceInformation -> {
            currentMaximumPriceInformation.update(productLoadDto.productId(), productLoadDto.price());
            highestPriceInformationRepository.updateById(
                    currentMaximumPriceInformation.getId(),
                    currentMaximumPriceInformation
            );
        };
    }

    private Runnable save(final ProductLoadDto productLoadDto) {
        return () -> {
            final CategoryLoadDto categoryLoadDto = categoryLoader.loadCategory(productLoadDto.categoryId())
                    .orElseThrow(() -> new InvalidCategoryIdException(productLoadDto.categoryId()));

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
            highestPriceInformationRepository.save(priceInformation);
        };
    }
}
