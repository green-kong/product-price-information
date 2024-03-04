package com.example.musinsaserver.priceinformation.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.priceinformation.application.port.in.LowestPriceInformationByBrandSearchUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;
import com.example.musinsaserver.priceinformation.application.port.out.loader.CategoryLoader;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MinimumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InsufficientDataInBrandException;

@Service
public class LowestPriceInformationByBrandSearchService implements LowestPriceInformationByBrandSearchUseCase {

    private final MinimumPriceInformationRepository minimumPriceInformationRepository;
    private final CategoryLoader categoryLoader;

    public LowestPriceInformationByBrandSearchService(
            final MinimumPriceInformationRepository minimumPriceInformationRepository,
            final CategoryLoader categoryLoader
    ) {
        this.minimumPriceInformationRepository = minimumPriceInformationRepository;
        this.categoryLoader = categoryLoader;
    }

    @Override
    public LowestPriceInformationByBrandResponses searchLowestPriceInformationByBrand(final Long brandId) {
        final List<PriceInformation> priceInformations = minimumPriceInformationRepository.findByBrandId(brandId);
        int countAllCategories = categoryLoader.countAllCategories();
        if (countAllCategories != priceInformations.size()) {
            throw new InsufficientDataInBrandException(brandId, countAllCategories, priceInformations.size());
        }
        return LowestPriceInformationByBrandResponses.from(priceInformations);
    }
}
