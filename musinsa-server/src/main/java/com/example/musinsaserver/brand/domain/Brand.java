package com.example.musinsaserver.brand.domain;

public class Brand {
    private final Long id;
    private final Name name;

    private Brand(final Long id, final Name name) {
        this.id = id;
        this.name = name;
    }

    public static Brand createWithoutId(final String name) {
        return new Brand(null, Name.from(name));
    }

    public static Brand createWithId(final Long id, final String name) {
        return new Brand(id, Name.from(name));
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }
}
