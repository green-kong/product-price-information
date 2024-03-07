package com.example.musinsaserver.brand.adaptor.out.persistence.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBrandAdaptor extends JpaRepository<JpaBrandEntity, Long> {

    Optional<JpaBrandEntity> findJpaBrandEntityByName(final String name);

    List<JpaBrandEntity> findByIdIn(final List<Long> ids);

}
