package com.example.musinsaserver.product.domain;

import com.example.musinsaserver.product.exception.InvalidPriceException;

public class Price {
    public static final int MINIMUM_PRICE = 10;
    public static final int MAXIMUM_PRICE = 1_000_000;
    private final int value;

    private Price(final int value) {
        this.value = value;
    }

    public static Price from(final int value) {
        if (value < MINIMUM_PRICE || value > MAXIMUM_PRICE) {
            throw new InvalidPriceException(value);
        }
        return new Price(value);
    }

    public int getValue() {
        return value;
    }
}
