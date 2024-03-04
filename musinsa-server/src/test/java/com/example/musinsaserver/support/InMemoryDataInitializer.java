package com.example.musinsaserver.support;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.musinsaserver.brand.adaptor.out.persistence.inmemory.InMemoryBrandRepository;
import com.example.musinsaserver.category.adaptor.port.out.persistence.InMemoryCategoryRepository;
import com.example.musinsaserver.priceinformation.adaptor.out.persistence.InMemoryMaximumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.adaptor.out.persistence.InMemoryMinimumPriceInformationRepository;
import com.example.musinsaserver.product.adaptor.out.persistence.InMemoryProductRepository;

@Component
@Profile("test")
public class InMemoryDataInitializer implements DataInitializer {

    private InMemoryCategoryRepository categoryRepository;
    private InMemoryProductRepository productRepository;
    private InMemoryBrandRepository brandRepository;
    private InMemoryMinimumPriceInformationRepository minimumPriceInformationRepository;
    private InMemoryMaximumPriceInformationRepository maximumPriceInformationRepository;

    public InMemoryDataInitializer(
            final InMemoryCategoryRepository categoryRepository,
            final InMemoryProductRepository productRepository,
            final InMemoryBrandRepository brandRepository,
            final InMemoryMinimumPriceInformationRepository minimumPriceInformationRepository,
            final InMemoryMaximumPriceInformationRepository maximumPriceInformationRepository
    ) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.minimumPriceInformationRepository = minimumPriceInformationRepository;
        this.maximumPriceInformationRepository = maximumPriceInformationRepository;
    }

    @Override
    public void initializeDatabase() {
        categoryRepository.clear();
        productRepository.clear();
        brandRepository.clear();
        minimumPriceInformationRepository.clear();
        maximumPriceInformationRepository.clear();
    }
}
