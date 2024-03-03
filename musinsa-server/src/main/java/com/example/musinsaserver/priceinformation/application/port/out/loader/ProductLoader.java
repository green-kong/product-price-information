package com.example.musinsaserver.priceinformation.application.port.out.loader;

import java.util.Optional;

import com.example.musinsaserver.priceinformation.application.port.out.loader.dto.ProductLoadDto;

public interface ProductLoader {

    Optional<ProductLoadDto> loadProduct(final Long id);

}
