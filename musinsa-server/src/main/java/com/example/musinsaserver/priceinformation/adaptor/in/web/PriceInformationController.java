package com.example.musinsaserver.priceinformation.adaptor.in.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.musinsaserver.priceinformation.application.port.in.HighestAndLowestPriceInformationSearchUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.LowestPriceInformationByBrandSearchUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.LowestPriceInformationByCategorySearchUseCase;
import com.example.musinsaserver.priceinformation.application.port.in.dto.HighestAndLowestPriceInformationByCategoryResponse;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByBrandResponses;
import com.example.musinsaserver.priceinformation.application.port.in.dto.LowestPriceInformationByCategoryResponse;

@RequestMapping("api/price-informations")
@RestController
public class PriceInformationController {

    private final LowestPriceInformationByBrandSearchUseCase lowestPriceInformationByBrandSearchUseCase;
    private final LowestPriceInformationByCategorySearchUseCase lowestPriceInformationByCategorySearchUseCase;
    private final HighestAndLowestPriceInformationSearchUseCase highestAndLowestPriceInformationSearchUseCase;

    public PriceInformationController(
            final LowestPriceInformationByBrandSearchUseCase lowestPriceInformationByBrandSearchUseCase,
            final LowestPriceInformationByCategorySearchUseCase lowestPriceInformationByCategorySearchUseCase,
            final HighestAndLowestPriceInformationSearchUseCase highestAndLowestPriceInformationSearchUseCase
    ) {
        this.lowestPriceInformationByBrandSearchUseCase = lowestPriceInformationByBrandSearchUseCase;
        this.lowestPriceInformationByCategorySearchUseCase = lowestPriceInformationByCategorySearchUseCase;
        this.highestAndLowestPriceInformationSearchUseCase = highestAndLowestPriceInformationSearchUseCase;
    }

    @GetMapping("brands/{brandId}/lowest")
    public ResponseEntity<LowestPriceInformationByBrandResponses> getLowestPriceProductInformationByBrand(
            @PathVariable("brandId") final Long brandId
    ) {
        final var response = lowestPriceInformationByBrandSearchUseCase.searchLowestPriceInformationByBrand(brandId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("categories/lowest")
    public ResponseEntity<LowestPriceInformationByCategoryResponse> getLowestPriceProductInformationByCategory() {
        final var response = lowestPriceInformationByCategorySearchUseCase.searchLowestPriceInformationByCategory();
        return ResponseEntity.ok(response);
    }

    @GetMapping("categories/{categoryName}/highest-lowest")
    public ResponseEntity<HighestAndLowestPriceInformationByCategoryResponse> getHighestAndLowestInformationByCategory(
            @PathVariable("categoryName") final String category
    ) {
        final var response = highestAndLowestPriceInformationSearchUseCase.searchHighestAndLowestPriceInformation(
                category);
        return ResponseEntity.ok(response);
    }
}
