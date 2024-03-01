package com.example.musinsaserver.product.domain;

public class Product {

    private Long id;
    private Price price;
    private Long brandId;

    private Product(final Long id, final Price price, final Long brandId) {
        this.id = id;
        this.price = price;
        this.brandId = brandId;
    }

    public static Product createWithoutId(final int price, final Long brandId) {
        return new Product(null, Price.from(price), brandId);
    }

    public static Product createWithId(final Long id, final int price, final Long brandId) {
        return new Product(id, Price.from(price), brandId);
    }

    public int getPriceValue() {
        return price.getValue();
    }

    public Long getId() {
        return id;
    }

    public Price getPrice() {
        return price;
    }

    public Long getBrandId() {
        return brandId;
    }
}
