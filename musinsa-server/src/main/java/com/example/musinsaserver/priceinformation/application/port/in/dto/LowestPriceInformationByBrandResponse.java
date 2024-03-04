package com.example.musinsaserver.priceinformation.application.port.in.dto;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

public record LowestPriceInformationByBrandResponse(String category, int price) {
    public static LowestPriceInformationByBrandResponse from(final PriceInformation priceInformation) {
        return new LowestPriceInformationByBrandResponse(priceInformation.getCategory(), priceInformation.getPrice());
    }
}
