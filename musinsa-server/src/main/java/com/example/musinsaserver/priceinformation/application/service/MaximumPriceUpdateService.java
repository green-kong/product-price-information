package com.example.musinsaserver.priceinformation.application.service;

import java.util.function.Consumer;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.priceinformation.application.port.in.MaximumPriceUpdateUseCase;
import com.example.musinsaserver.priceinformation.application.port.out.loader.BrandLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.ProductLoader;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.BrandLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MaximumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InvalidBrandIdException;
import com.example.musinsaserver.priceinformation.exception.InvalidProductIdException;
import com.example.musinsaserver.product.application.port.out.event.dto.ProductRegisterEvent;

// TODO: 2024/03/04 중복코드 관리
@Service
public class MaximumPriceUpdateService implements MaximumPriceUpdateUseCase {

    private final MaximumPriceInformationRepository maximumPriceInformationRepository;
    private final ProductLoader productLoader;
    private final BrandLoader brandLoader;

    public MaximumPriceUpdateService(
            final MaximumPriceInformationRepository maximumPriceInformationRepository,
            final ProductLoader productLoader,
            final BrandLoader brandLoader
    ) {
        this.maximumPriceInformationRepository = maximumPriceInformationRepository;
        this.productLoader = productLoader;
        this.brandLoader = brandLoader;
    }

    @Override
    public void updateMaximumPriceUpdate(final ProductRegisterEvent productRegisterEvent) {
        final Long productId = productRegisterEvent.productId();
        final ProductLoadDto productLoadDto = productLoader.loadProduct(productId)
                .orElseThrow(() -> new InvalidProductIdException(productId));
        maximumPriceInformationRepository.findByBrandIdAndCategory(
                productLoadDto.brandId(),
                productLoadDto.category()
        ).ifPresentOrElse(comparePriceAndUpdate(productLoadDto), saveRegisteredProductAsMaximum(productLoadDto));
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
            final BrandLoadDto brandLoadDto = brandLoader.loadBrand(productLoadDto.brandId())
                    .orElseThrow(() -> new InvalidBrandIdException(productLoadDto.brandId()));
            final PriceInformation priceInformation = PriceInformation.createWithoutId(
                    productLoadDto.productId(),
                    productLoadDto.brandId(),
                    productLoadDto.category(),
                    productLoadDto.price(),
                    brandLoadDto.brandName()
            );
            maximumPriceInformationRepository.save(priceInformation);
        };
    }
}
