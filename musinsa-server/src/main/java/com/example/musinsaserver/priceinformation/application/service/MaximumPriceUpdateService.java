package com.example.musinsaserver.priceinformation.application.service;

import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceUpdateUseCase;
import com.example.musinsaserver.priceinformation.application.port.out.loader.BrandLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.CategoryLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.BrandLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MaximumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidCategoryIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidProductIdException;

// TODO: 2024/03/04 중복코드 관리
@Service
public class MaximumPriceUpdateService implements MaximumPriceUpdateUseCase {

    private final MaximumPriceInformationRepository maximumPriceInformationRepository;
    private final ProductLoader productLoader;
    private final BrandLoader brandLoader;
    private final CategoryLoader categoryLoader;

    public MaximumPriceUpdateService(
            final MaximumPriceInformationRepository maximumPriceInformationRepository,
            final ProductLoader productLoader,
            final BrandLoader brandLoader,
            final CategoryLoader categoryLoader
    ) {
        this.maximumPriceInformationRepository = maximumPriceInformationRepository;
        this.productLoader = productLoader;
        this.brandLoader = brandLoader;
        this.categoryLoader = categoryLoader;
    }

    @Override
    public void updateMaximumPrice(final Long productId) {
        final ProductLoadDto productLoadDto = productLoader.loadProduct(productId)
                .orElseThrow(() -> new InvalidProductIdException(productId));
        final Optional<PriceInformation> byBrandIdAndCategoryId = maximumPriceInformationRepository.findByBrandIdAndCategoryId(
                productLoadDto.brandId(),
                productLoadDto.categoryId()
        );
        byBrandIdAndCategoryId.ifPresentOrElse(comparePriceAndUpdate(productLoadDto),
                saveRegisteredProductAsMaximum(productLoadDto));
    }

    private Consumer<PriceInformation> comparePriceAndUpdate(final ProductLoadDto productLoadDto) {
        return currentMaximumPriceInformation -> {
            if (currentMaximumPriceInformation.isCheaperThan(productLoadDto.price())) {
                updateMaximumPriceInformation(productLoadDto, currentMaximumPriceInformation);
            }
        };
    }

    private void updateMaximumPriceInformation(
            final ProductLoadDto productLoadDto,
            final PriceInformation currentMaximumPriceInformation
    ) {
        currentMaximumPriceInformation.update(productLoadDto.productId(), productLoadDto.price());
        maximumPriceInformationRepository.updateById(
                currentMaximumPriceInformation.getId(),
                currentMaximumPriceInformation
        );
    }

    private Runnable saveRegisteredProductAsMaximum(final ProductLoadDto productLoadDto) {
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
            maximumPriceInformationRepository.save(priceInformation);
        };
    }
}
