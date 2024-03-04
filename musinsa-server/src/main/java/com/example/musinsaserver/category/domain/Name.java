package com.example.musinsaserver.category.domain;

import static org.apache.logging.log4j.util.Strings.isBlank;

import com.example.musinsaserver.category.exception.InvalidCategoryNameException;

public class Name {
    public static final int MINIMUM_LENGTH = 2;
    private String value;

    private Name(final String value) {
        this.value = value;
    }

    public static Name from(final String value) {
        if (isBlank(value) || value.length()< MINIMUM_LENGTH) {
            throw new InvalidCategoryNameException(value);
        }
        return new Name(value);
    }

    public String getValue() {
        return value;
    }
}
