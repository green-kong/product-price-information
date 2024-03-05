package com.example.musinsaserver.priceinformation.adaptor.out.persistence.jpa.lowest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.adaptor.out.persistence.JpaPriceInformationMapper;
import com.example.musinsaserver.priceinformation.application.port.out.persistence.LowestPriceInformationRepository;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;

@Component
public class JpaLowestPriceInformationRepository implements LowestPriceInformationRepository {

    private final JpaLowestPriceInformationAdaptor informations;
    private final JpaPriceInformationMapper mapper;

    public JpaLowestPriceInformationRepository(
            final JpaLowestPriceInformationAdaptor informations,
            final JpaPriceInformationMapper mapper
    ) {
        this.informations = informations;
        this.mapper = mapper;
    }

    @Override
    public List<PriceInformation> findByBrandId(final Long brandId) {
        final var entities = informations.findJpaLowestPriceInformationEntitiesByBrandId(brandId);
        return entities.stream()
                .map(mapper::toPriceInformationDomainEntity)
                .toList();
    }

    @Override
    public PriceInformation save(final PriceInformation priceInformation) {
        final var jpaEntity = mapper.toJpaLowestInformation(priceInformation);
        informations.save(jpaEntity);
        return mapper.toPriceInformationDomainEntity(jpaEntity);
    }

    @Override
    public Optional<PriceInformation> findById(final Long id) {
        final JpaLowestPriceInformationEntity jpaEntity = informations.findById(id)
                .orElse(null);
        if (Objects.isNull(jpaEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPriceInformationDomainEntity(jpaEntity));
    }

    @Override
    public Optional<PriceInformation> findByBrandIdAndCategoryId(final Long brandId, final Long categoryId) {
        final var jpaEntity = informations.findJpaLowestPriceInformationEntityByBrandIdAndCategoryId(brandId,
                        categoryId)
                .orElse(null);
        if (Objects.isNull(jpaEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPriceInformationDomainEntity(jpaEntity));
    }

    @Override
    public void updateById(final Long id, final PriceInformation priceInformation) {
        final var jpaEntity = mapper.toJpaLowestInformation(priceInformation);
        informations.save(jpaEntity);
    }

    @Override
    public Optional<PriceInformation> findByProductId(final Long productId) {
        final var jpaEntity = informations.findJpaLowestPriceInformationEntityByProductId(productId)
                .orElse(null);
        if (Objects.isNull(jpaEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPriceInformationDomainEntity(jpaEntity));
    }

    @Override
    public void deleteById(final Long id) {
        informations.deleteById(id);
    }

    @Override
    public List<PriceInformation> findLowestPriceInformationByCategoryIds(final List<Long> categoryIds) {
        final List<Optional<JpaLowestPriceInformationEntity>> jpaEntities = categoryIds.stream()
                .map(informations::findFirstByCategoryIdOrderByPriceAsc)
                .toList();
        final List<PriceInformation> priceInformations = new ArrayList<>();
        for (final Optional<JpaLowestPriceInformationEntity> jpaEntity : jpaEntities) {
            jpaEntity.ifPresent(jpaLowestPriceInformationEntity -> priceInformations.add(
                    mapper.toPriceInformationDomainEntity(jpaLowestPriceInformationEntity)));
        }
        return priceInformations;
    }

    @Override
    public Optional<PriceInformation> findEndPriceInformationByCategoryId(final Long categoryId) {
        final var jpaEntity = informations.findFirstByCategoryIdOrderByPriceAsc(categoryId)
                .orElse(null);
        if (Objects.isNull(jpaEntity)) {
            return Optional.empty();
        }
        return Optional.of(mapper.toPriceInformationDomainEntity(jpaEntity));
    }
}
