package com.example.musinsaserver.brand.adaptor.out.persistence.jpa;

import org.springframework.stereotype.Component;

import com.example.musinsaserver.brand.domain.Brand;

@Component
public class JpaBrandMapper {

    public JpaBrandEntity toJpaBrandEntity(final Brand brand) {
        return new JpaBrandEntity(brand.getNameValue());
    }

    public Brand toBrandDomainEntity(final JpaBrandEntity jpaBrandEntity) {
        return Brand.createWithId(jpaBrandEntity.getId(), jpaBrandEntity.getName());
    }
}
