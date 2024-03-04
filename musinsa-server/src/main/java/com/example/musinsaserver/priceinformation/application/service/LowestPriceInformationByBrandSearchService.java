package com.example.musinsaserver.priceinformation.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.musinsaserver.priceinformation.application.port.in.LowestPriceInformationByBrandSearchUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.MinimumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;

@Service
public class LowestPriceInformationByBrandSearchService implements LowestPriceInformationByBrandSearchUseCase {

    private final MinimumPriceInformationRepository minimumPriceInformationRepository;

    public LowestPriceInformationByBrandSearchService(
            final MinimumPriceInformationRepository minimumPriceInformationRepository
    ) {
        this.minimumPriceInformationRepository = minimumPriceInformationRepository;
    }

    @Override
    public LowestPriceInformationByBrandResponses searchLowestPriceInformationByBrand(final Long brandId) {
        // TODO: 2024/03/04 카테고리 영속화 이후, category 개수와 일치하는지 validate
        final List<PriceInformation> priceInformations = minimumPriceInformationRepository.findByBrandId(brandId);
        return LowestPriceInformationByBrandResponses.from(priceInformations);
    }
}
