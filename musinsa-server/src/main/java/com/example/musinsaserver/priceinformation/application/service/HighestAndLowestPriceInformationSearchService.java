package com.example.musinsaserver.priceinformation.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.common.loader.CategoryLoader;
import com.example.musinsaserver.common.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.priceinformation.application.port.in.HighestAndLowestPriceInformationSearchUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.dto.HighestAndLowestPriceInformationByCategoryResponse;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.HighestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.NonExistentCategoryNameException;
import com.example.musinsaserver.priceinformation.exception.NonExistentHighestInformation;
import com.example.musinsaserver.priceinformation.exception.NonExistentLowestInformation;

@Service
@Transactional(readOnly = true)
public class HighestAndLowestPriceInformationSearchService implements HighestAndLowestPriceInformationSearchUseCase {

    private final CategoryLoader categoryLoader;
    private final HighestPriceInformationRepository highestRepository;
    private final LowestPriceInformationRepository lowestRepository;

    public HighestAndLowestPriceInformationSearchService(
            final CategoryLoader categoryLoader,
            final HighestPriceInformationRepository highestRepository,
            final LowestPriceInformationRepository lowestRepository
    ) {
        this.categoryLoader = categoryLoader;
        this.highestRepository = highestRepository;
        this.lowestRepository = lowestRepository;
    }

    @Override
    public HighestAndLowestPriceInformationByCategoryResponse searchHighestAndLowestPriceInformation(
            final String category
    ) {
        final CategoryLoadDto categoryLoadDto = getCategoryLoadDtoOrThrow(category);
        final PriceInformation highestPriceInformation = getHighestPriceInformationOrThrow(categoryLoadDto);
        final PriceInformation lowestPriceInformation = getLowestPriceInformationOrThrow(categoryLoadDto);
        return HighestAndLowestPriceInformationByCategoryResponse.of(
                category,
                highestPriceInformation,
                lowestPriceInformation
        );
    }

    private PriceInformation getLowestPriceInformationOrThrow(final CategoryLoadDto categoryLoadDto) {
        return lowestRepository.findEndPriceInformationByCategoryId(categoryLoadDto.id())
                .orElseThrow(() -> new NonExistentLowestInformation(categoryLoadDto.id()));
    }

    private PriceInformation getHighestPriceInformationOrThrow(final CategoryLoadDto categoryLoadDto) {
        return highestRepository.findEndPriceInformationByCategoryId(categoryLoadDto.id())
                .orElseThrow(() -> new NonExistentHighestInformation(categoryLoadDto.id()));
    }

    private CategoryLoadDto getCategoryLoadDtoOrThrow(final String category) {
        return categoryLoader.loadCategory(category)
                .orElseThrow(() -> new NonExistentCategoryNameException(category));
    }

}
