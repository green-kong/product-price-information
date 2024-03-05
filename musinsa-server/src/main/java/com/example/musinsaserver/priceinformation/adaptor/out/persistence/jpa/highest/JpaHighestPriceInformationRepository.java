package com.example.musinsaserver.priceinformation.adaptor.out.persistence.jpa.highest;

import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.adaptor.out.persistence.JpaPriceInformationMapper;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.HighestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;

@Component
public class JpaHighestPriceInformationRepository implements HighestPriceInformationRepository {

    private final JpaHighestPriceInformationAdaptor highestPriceInformations;
    private final JpaPriceInformationMapper mapper;

    public JpaHighestPriceInformationRepository(
            final JpaHighestPriceInformationAdaptor highestPriceInformations,
            final JpaPriceInformationMapper mapper
    ) {
        this.highestPriceInformations = highestPriceInformations;
        this.mapper = mapper;
    }

    @Override
    public PriceInformation save(final PriceInformation priceInformation) {
        final JpaHighestPriceInformationEntity jpaEntity = mapper.toJpaHighestInformation(priceInformation);
        highestPriceInformations.save(jpaEntity);
        return mapper.toPriceInformationDomainEntity(jpaEntity);
    }

    @Override
    public Optional<PriceInformation> findById(final Long id) {
        final JpaHighestPriceInformationEntity jpaEntity = highestPriceInformations.findById(id)
                .orElse(null);
        if (Objects.isNull(jpaEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPriceInformationDomainEntity(jpaEntity));
    }

    @Override
    public Optional<PriceInformation> findByBrandIdAndCategoryId(final Long brandId, final Long categoryId) {
        final var jpaEntity = highestPriceInformations.findJpaHighestPriceInformationEntityByBrandIdAndCategoryId(
                        brandId, categoryId)
                .orElse(null);
        if (Objects.isNull(jpaEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPriceInformationDomainEntity(jpaEntity));
    }

    @Override
    public void updateById(final Long id, final PriceInformation priceInformation) {
        final var jpaEntity = mapper.toJpaHighestInformation(priceInformation);
        highestPriceInformations.save(jpaEntity);
    }

    @Override
    public Optional<PriceInformation> findByProductId(final Long productId) {
        final var jpaEntity = highestPriceInformations.findJpaHighestPriceInformationEntityByProductId(productId)
                .orElse(null);
        if (Objects.isNull(jpaEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPriceInformationDomainEntity(jpaEntity));
    }

    @Override
    public void deleteById(final Long id) {
        highestPriceInformations.deleteById(id);
    }
}
