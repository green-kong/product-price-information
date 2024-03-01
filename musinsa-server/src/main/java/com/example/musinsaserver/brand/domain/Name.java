package com.example.musinsaserver.brand.domain;

import static org.apache.logging.log4j.util.Strings.isBlank;

import com.example.musinsaserver.brand.exception.InvalidBrandNameException;

public class Name {
    private final String name;

    private Name(final String name) {
        this.name = name;
    }

    public static Name from(final String name) {
        if (isBlank(name)) {
            throw new InvalidBrandNameException(name);
        }
        return new Name(name);
    }

    public String getName() {
        return name;
    }
}
