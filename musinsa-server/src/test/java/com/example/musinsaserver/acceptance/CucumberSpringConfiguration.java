package com.example.musinsaserver.acceptance;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestExecutionListeners;

import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;

@Import({CucumberClient.class})
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestExecutionListeners(listeners = {}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class CucumberSpringConfiguration {

    @LocalServerPort
    private int port;

    @Before
    public void before() {
        RestAssured.port = port;
    }
}
