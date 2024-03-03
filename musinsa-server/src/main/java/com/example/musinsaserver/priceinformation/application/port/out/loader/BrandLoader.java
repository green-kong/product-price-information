package com.example.musinsaserver.priceinformation.application.port.out.loader;

import java.util.Optional;

import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.BrandLoadDto;

public interface BrandLoader {
    Optional<BrandLoadDto> loadBrand(final Long brandId);
}
