package com.example.musinsaserver.priceinformation.adaptor.out.persistence;

import static java.util.Objects.isNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.application.port.out.persistence.MinimumPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;

@Component
public class InMemoryMinimumPriceInformationRepository implements MinimumPriceInformationRepository {

    private Long insertedId = 0L;
    private final Map<Long, PriceInformation> minimumPriceInformations = new HashMap<>();

    @Override
    public PriceInformation save(final PriceInformation minimumPriceInformation) {
        insertedId += 1;
        final PriceInformation minimumPriceInformationWithId = PriceInformation.createWithId(insertedId,
                minimumPriceInformation.getProductId(),
                minimumPriceInformation.getBrandId(), minimumPriceInformation.getCategory(),
                minimumPriceInformation.getPrice(), minimumPriceInformation.getBrandName());
        minimumPriceInformations.put(insertedId, minimumPriceInformationWithId);
        return minimumPriceInformationWithId;
    }

    @Override
    public Optional<PriceInformation> findById(final Long id) {
        final PriceInformation priceInformation = minimumPriceInformations.get(id);
        if (isNull(priceInformation)) {
            return Optional.empty();
        }
        return Optional.of(priceInformation);
    }

    @Override
    public Optional<PriceInformation> findByBrandIdAndCategory(final Long brandId, final String category) {
        return minimumPriceInformations.values()
                .stream()
                .filter(priceInformation -> priceInformation.getBrandId().equals(brandId)
                        && priceInformation.getCategory().equals(category))
                .findFirst();
    }

    @Override
    public void updateById(final Long id, final PriceInformation minimumPriceInformation) {
        minimumPriceInformations.put(id, minimumPriceInformation);
    }

    @Override
    public Optional<PriceInformation> findByProductId(final Long productId) {
        return minimumPriceInformations.values()
                .stream()
                .filter(priceInformation -> priceInformation.getProductId().equals(productId))
                .findFirst();
    }

    @Override
    public void deleteById(final Long id) {
        minimumPriceInformations.remove(id);
    }
}
