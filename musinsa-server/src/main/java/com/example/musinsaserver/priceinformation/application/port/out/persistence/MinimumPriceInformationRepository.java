package com.example.musinsaserver.priceinformation.application.port.out.persistence;

import java.util.Optional;

import com.example.musinsaserver.priceinformation.domain.PriceInformation;

public interface MinimumPriceInformationRepository {
    PriceInformation save(final PriceInformation minimumPriceInformation);

    Optional<PriceInformation> findById(final Long id);
}
