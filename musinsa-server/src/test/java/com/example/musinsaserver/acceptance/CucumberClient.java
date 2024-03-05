package com.example.musinsaserver.acceptance;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.test.context.TestComponent;

import io.cucumber.spring.ScenarioScope;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@TestComponent
@ScenarioScope
public class CucumberClient {
    private Map<String, Long> dataStorage = new HashMap<>();
    private ExtractableResponse<Response> response;

    public CucumberClient() {
    }

    public void addData(final String key, final Long value) {
        dataStorage.put(key, value);
    }

    public Long getData(final String key) {
        final Long id = dataStorage.get(key);
        if (Objects.isNull(id)) {
            return 0L;
        }
        return id;
    }

    public ExtractableResponse<Response> getResponse() {
        return response;
    }

    public void setResponse(final ExtractableResponse<Response> response) {
        this.response = response;
    }
}
