package com.example.musinsaserver.product.domain;

import com.example.musinsaserver.product.exception.InvalidCategoryException;

public enum Category {
    TOP("상의"),
    OUTER("아우터"),
    PANTS("하의"),
    SNEAKERS("스니커즈"),
    BAG("가방"),
    HAT("모자"),
    SOCKS("양말"),
    ACCESSORIES("액세서리");

    private final String value;

    Category(final String value) {
        this.value = value;
    }

    public static Category from(final String name) {
        try {
            return Category.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new InvalidCategoryException(name);
        }
    }
    public String getValue() {
        return value;
    }
}
