package com.example.musinsaserver.support;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Component
@Profile("test")
public class H2DataInitializer implements DataInitializer {
    private static final String OFF = "FALSE";
    private static final String ON = "TRUE";
    private static final String TRUNCATE = "TRUNCATE TABLE `%s` RESTART IDENTITY";

    @PersistenceContext
    private EntityManager em;

    private final List<String> tableNames = new ArrayList<>();

    @Override
    @BeforeEach
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void initializeDatabase() {
        if (tableNames.isEmpty()) {
            init();
        }
        setForeignKeyEnabled(OFF);
        truncateAllTables();
        setForeignKeyEnabled(ON);
    }

    private void setForeignKeyEnabled(final String enabled) {
        em.createNativeQuery("SET REFERENTIAL_INTEGRITY " + enabled).executeUpdate();
    }

    private void truncateAllTables() {
        tableNames.stream()
                .map(tableName -> em.createNativeQuery(String.format(TRUNCATE, tableName)))
                .forEach(Query::executeUpdate);
    }

    private void init() {
        final List<Object[]> results = em.createNativeQuery("SHOW TABLES").getResultList();
        results.forEach(result -> tableNames.add(result[0].toString()));
    }
}
