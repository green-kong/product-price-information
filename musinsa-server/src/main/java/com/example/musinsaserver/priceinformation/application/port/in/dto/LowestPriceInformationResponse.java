package com.example.musinsaserver.priceinformation.application.port.in.dto;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

public record LowestPriceInformationResponse(String category, String brand, int price) {
    public static LowestPriceInformationResponse from(final PriceInformation priceInformation) {
        return new LowestPriceInformationResponse(
                priceInformation.getCategory(),
                priceInformation.getBrandName(),
                priceInformation.getPrice()
        );
    }
}
