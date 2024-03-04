package com.example.musinsaserver.priceinformation.application.port.out.persistence;

import java.util.List;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

public interface MinimumPriceInformationRepository extends PriceInformationRepository {

    List<PriceInformation> findByBrandId(final Long brandId);
}
