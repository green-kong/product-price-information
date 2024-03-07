package com.example.musinsaserver.priceinformation.application.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.musinsaserver.priceinformation.application.port.in.LowestPriceInformationByCategorySearchUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByCategoryResponse;
import com.example.musinsaserver.common.application.port.out.loader.CategoryLoader;
import com.example.musinsaserver.common.application.port.out.loader.dto.CategoryLoadDto;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;
import com.example.musinsaserver.priceinformation.exception.InsufficientDataByCategoryException;

@Service
@Transactional(readOnly = true)
public class LowestPriceInformationByCategorySearchService implements LowestPriceInformationByCategorySearchUseCase {

    private final CategoryLoader categoryLoader;
    private final LowestPriceInformationRepository lowestPriceInformationRepository;

    public LowestPriceInformationByCategorySearchService(
            final CategoryLoader categoryLoader,
            final LowestPriceInformationRepository lowestPriceInformationRepository
    ) {
        this.categoryLoader = categoryLoader;
        this.lowestPriceInformationRepository = lowestPriceInformationRepository;
    }

    @Override
    public LowestPriceInformationByCategoryResponse searchLowestPriceInformationByCategory() {
        final List<Long> categoryIds = getCategoryIds();
        final List<PriceInformation> information = lowestPriceInformationRepository.findLowestPriceInformationByCategoryIds(
                categoryIds);
        validate(categoryIds, information);
        return LowestPriceInformationByCategoryResponse.from(information);
    }

    private static void validate(final List<Long> categoryIds, final List<PriceInformation> information) {
        if (information.size() != categoryIds.size()) {
            throw new InsufficientDataByCategoryException(categoryIds.size(), information.size());
        }
    }

    private List<Long> getCategoryIds() {
        final List<CategoryLoadDto> categoryLoadDtos = categoryLoader.loadAllCategories();
        return categoryLoadDtos.stream()
                .map(CategoryLoadDto::id)
                .toList();
    }
}
