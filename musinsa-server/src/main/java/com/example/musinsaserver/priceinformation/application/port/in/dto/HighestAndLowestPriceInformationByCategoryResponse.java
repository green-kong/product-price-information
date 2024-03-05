package com.example.musinsaserver.priceinformation.application.port.in.dto;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

public record HighestAndLowestPriceInformationByCategoryResponse(
        String category,
        BrandAndPriceInformationResponse highest,
        BrandAndPriceInformationResponse lowest
) {
    public static HighestAndLowestPriceInformationByCategoryResponse of(
            final String category,
            final PriceInformation highestPriceInformation,
            final PriceInformation lowestPriceInformation
    ) {
        final var highest = BrandAndPriceInformationResponse.from(highestPriceInformation);
        final var lowest = BrandAndPriceInformationResponse.from(lowestPriceInformation);
        return new HighestAndLowestPriceInformationByCategoryResponse(category, highest, lowest);
    }
}
