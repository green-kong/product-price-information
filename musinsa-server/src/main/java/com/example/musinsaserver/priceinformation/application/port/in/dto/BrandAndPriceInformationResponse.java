package com.example.musinsaserver.priceinformation.application.port.in.dto;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

public record BrandAndPriceInformationResponse(String brand, int price) {
    public static BrandAndPriceInformationResponse from(final PriceInformation priceInformation) {
        return new BrandAndPriceInformationResponse(priceInformation.getBrandName(), priceInformation.getPrice());
    }
}
