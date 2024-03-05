package com.example.musinsaserver.brand.adaptor.out.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaBrandAdaptor extends JpaRepository<JpaBrandEntity, Long> {

    Optional<JpaBrandEntity> findJpaBrandEntityByName(final String name);

}
