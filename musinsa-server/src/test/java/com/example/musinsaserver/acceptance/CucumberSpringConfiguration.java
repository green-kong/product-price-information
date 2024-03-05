package com.example.musinsaserver.acceptance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.musinsaserver.support.DataInitializer;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;

@ActiveProfiles("test")
@Import({CucumberClient.class})
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CucumberSpringConfiguration {

    @LocalServerPort
    private int port;

    @Autowired
    DataInitializer dataInitializer;

    @Before
    public void before() {
        RestAssured.port = port;
        dataInitializer.initializeDatabase();
    }
}
