package com.example.musinsaserver.category.adaptor.port.out.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryAdaptor extends JpaRepository<JpaCategoryEntity, Long> {
    Optional<JpaCategoryEntity> findJpaCategoryEntityByName(final String name);
}
