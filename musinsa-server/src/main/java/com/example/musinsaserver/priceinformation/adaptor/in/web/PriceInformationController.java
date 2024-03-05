package com.example.musinsaserver.priceinformation.adaptor.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.musinsaserver.priceinformation.application.port.in.LowestPriceInformationByBrandSearchUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;

@RequestMapping("api/priceInformations")
public class PriceInformationController {

    private final LowestPriceInformationByBrandSearchUseCase lowestPriceInformationByBrandSearchUseCase;

    public PriceInformationController(
            final LowestPriceInformationByBrandSearchUseCase lowestPriceInformationByBrandSearchUseCase) {
        this.lowestPriceInformationByBrandSearchUseCase = lowestPriceInformationByBrandSearchUseCase;
    }

    // todo 테스트 작성해야함
    @GetMapping("brands/{brandId}")
    public ResponseEntity<LowestPriceInformationByBrandResponses> getLowestPriceProductInformationByBrand(
            @PathVariable("brandId") final Long brandId
    ) {
        final LowestPriceInformationByBrandResponses lowestPriceInformationByBrandResponses = lowestPriceInformationByBrandSearchUseCase.searchLowestPriceInformationByBrand(
                brandId);
        return ResponseEntity.ok(lowestPriceInformationByBrandResponses);
    }
}
