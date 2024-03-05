package com.example.musinsaserver.priceinformation.application.port.in;

import com.example.musinsaserver.priceinformation.application.port.in.dto.HighestAndLowestPriceInformationByCategoryResponse;

public interface HighestAndLowestPriceInformationSearchUseCase {
    HighestAndLowestPriceInformationByCategoryResponse searchHighestAndLowestPriceInformation(final String category);
}
