package com.example.musinsaserver.category.adaptor.port.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaCategoryAdaptor extends JpaRepository<JpaCategoryEntity, Long> {
}
