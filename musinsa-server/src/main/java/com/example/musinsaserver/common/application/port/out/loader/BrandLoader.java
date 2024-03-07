package com.example.musinsaserver.common.application.port.out.loader;

import java.util.List;
import java.util.Optional;

import com.example.musinsaserver.common.application.port.out.loader.dto.BrandLoadDto;

public interface BrandLoader {
    Optional<BrandLoadDto> loadBrand(final Long brandId);

    List<BrandLoadDto> loadBrandByIds(List<Long> brandIds);
}
