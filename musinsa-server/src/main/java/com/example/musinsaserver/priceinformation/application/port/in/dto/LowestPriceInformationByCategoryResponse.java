package com.example.musinsaserver.priceinformation.application.port.in.dto;

import java.util.List;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

public record LowestPriceInformationByCategoryResponse(
        List<LowestPriceInformationResponse> lowestPriceInformationResponses,
        int sum
) {
    public static LowestPriceInformationByCategoryResponse from(List<PriceInformation> priceInformations) {
        final int sum = priceInformations.stream()
                .mapToInt(PriceInformation::getPrice)
                .sum();
        final List<LowestPriceInformationResponse> lowestPriceInformationResponses = priceInformations.stream()
                .map(LowestPriceInformationResponse::from)
                .toList();
        return new LowestPriceInformationByCategoryResponse(lowestPriceInformationResponses, sum);
    }
}
