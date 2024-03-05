package com.example.musinsaserver.priceinformation.adaptor.out.persistence;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.priceinformation.adaptor.out.persistence.jpa.highest.JpaHighestPriceInformationEntity;
import com.example.musinsaserver.priceinformation.adaptor.out.persistence.jpa.lowest.JpaLowestPriceInformationEntity;
import com.example.musinsaserver.priceinformation.domain.PriceInformation;

@Component
public class JpaPriceInformationMapper {

    public JpaHighestPriceInformationEntity toJpaHighestInformation(final PriceInformation priceInformation) {
        return new JpaHighestPriceInformationEntity(
                priceInformation.getId(),
                priceInformation.getProductId(),
                priceInformation.getBrandId(),
                priceInformation.getCategoryId(),
                priceInformation.getCategory(),
                priceInformation.getPrice(),
                priceInformation.getBrandName()
        );
    }

    public PriceInformation toPriceInformationDomainEntity(
            final JpaHighestPriceInformationEntity jpaHighestPriceInformationEntity
    ) {
        return PriceInformation.createWithId(
                jpaHighestPriceInformationEntity.getId(),
                jpaHighestPriceInformationEntity.getProductId(),
                jpaHighestPriceInformationEntity.getCategoryId(),
                jpaHighestPriceInformationEntity.getBrandId(),
                jpaHighestPriceInformationEntity.getCategory(),
                jpaHighestPriceInformationEntity.getPrice(),
                jpaHighestPriceInformationEntity.getBrandName()
        );
    }

    public JpaLowestPriceInformationEntity toJpaLowestInformation(final PriceInformation priceInformation) {
        return new JpaLowestPriceInformationEntity(
                priceInformation.getId(),
                priceInformation.getProductId(),
                priceInformation.getBrandId(),
                priceInformation.getCategoryId(),
                priceInformation.getCategory(),
                priceInformation.getPrice(),
                priceInformation.getBrandName()
        );
    }

    public PriceInformation toPriceInformationDomainEntity(
            final JpaLowestPriceInformationEntity jpaLowestPriceInformationEntity
    ) {
        return PriceInformation.createWithId(
                jpaLowestPriceInformationEntity.getId(),
                jpaLowestPriceInformationEntity.getProductId(),
                jpaLowestPriceInformationEntity.getCategoryId(),
                jpaLowestPriceInformationEntity.getBrandId(),
                jpaLowestPriceInformationEntity.getCategory(),
                jpaLowestPriceInformationEntity.getPrice(),
                jpaLowestPriceInformationEntity.getBrandName()
        );
    }
}
