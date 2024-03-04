package com.example.musinsaserver.priceinformation.application.port.in;

import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;

public interface LowestPriceInformationByBrandSearchUseCase {
    LowestPriceInformationByBrandResponses searchLowestPriceInformationByBrand(final Long brandId);
}
