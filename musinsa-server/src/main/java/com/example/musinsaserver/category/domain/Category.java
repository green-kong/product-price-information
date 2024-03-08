package com.example.musinsaserver.category.domain;

public class Category {
    private Long id;
    private Name name;

    private Category(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }

    public static Category createWithoutId(final String value) {
        return new Category(null, Name.from(value));
    }

    public static Category createWithId(final Long id, final String value) {
        return new Category(id, Name.from(value));
    }

    public String getNameValue() {
        return name.getValue();
    }

    public Long getId() {
        return id;
    }
}
