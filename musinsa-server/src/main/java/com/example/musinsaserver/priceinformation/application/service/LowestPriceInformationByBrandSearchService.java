package com.example.musinsaserver.priceinformation.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.priceinformation.application.port.in.LowestPriceInformationByBrandSearchUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;
import com.example.musinsaserver.common.loader.CategoryLoader;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InsufficientDataInBrandException;

@Service
@Transactional(readOnly = true)
public class LowestPriceInformationByBrandSearchService implements LowestPriceInformationByBrandSearchUseCase {

    private final LowestPriceInformationRepository lowestPriceInformationRepository;
    private final CategoryLoader categoryLoader;

    public LowestPriceInformationByBrandSearchService(
            final LowestPriceInformationRepository lowestPriceInformationRepository,
            final CategoryLoader categoryLoader
    ) {
        this.lowestPriceInformationRepository = lowestPriceInformationRepository;
        this.categoryLoader = categoryLoader;
    }

    @Override
    public LowestPriceInformationByBrandResponses searchLowestPriceInformationByBrand(final Long brandId) {
        final List<PriceInformation> priceInformations = lowestPriceInformationRepository.findByBrandId(brandId);
        int countAllCategories = categoryLoader.countAllCategories();
        if (countAllCategories != priceInformations.size()) {
            throw new InsufficientDataInBrandException(brandId, countAllCategories, priceInformations.size());
        }
        return LowestPriceInformationByBrandResponses.from(priceInformations);
    }
}
