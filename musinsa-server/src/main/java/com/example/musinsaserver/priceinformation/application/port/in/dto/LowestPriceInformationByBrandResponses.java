package com.example.musinsaserver.priceinformation.application.port.in.dto;

import java.util.List;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

public record LowestPriceInformationByBrandResponses(
        String brand,
        List<LowestPriceInformationByBrandResponse> lowestPriceInformationResponses,
        int sum
) {

    private static final int FIRST_INDEX = 0;

    public static LowestPriceInformationByBrandResponses from(final List<PriceInformation> priceInformations) {
        final String brandName = priceInformations.get(FIRST_INDEX).getBrandName();
        final List<LowestPriceInformationByBrandResponse> lowestPriceInformationResponses = getLowestPriceInformationResponses(
                priceInformations);
        final int sum = calculateSum(priceInformations);
        return new LowestPriceInformationByBrandResponses(brandName, lowestPriceInformationResponses, sum);
    }

    private static List<LowestPriceInformationByBrandResponse> getLowestPriceInformationResponses(
            final List<PriceInformation> priceInformations) {
        return priceInformations.stream()
                .map(LowestPriceInformationByBrandResponse::from)
                .toList();
    }

    private static int calculateSum(final List<PriceInformation> priceInformations) {
        return priceInformations.stream()
                .mapToInt(PriceInformation::getPrice)
                .sum();
    }
}
