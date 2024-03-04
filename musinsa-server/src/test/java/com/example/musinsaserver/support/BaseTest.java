package com.example.musinsaserver.support;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseTest {

    @Autowired
    DataInitializer dataInitializer;

    @BeforeEach
    void setUp() {
        dataInitializer.initializeDatabase();
    }
}
