package com.example.musinsaserver.product.domain;

public class Product {

    private Long id;
    private Category category;
    private Price price;
    private Long brandId;

    private Product(final Long id, final Category category, final Price price, final Long brandId) {
        this.id = id;
        this.category = category;
        this.price = price;
        this.brandId = brandId;
    }

    public static Product createWithoutId(final int price, final Category category, final Long brandId) {
        return new Product(null, category, Price.from(price), brandId);
    }

    public static Product createWithId(final Long id, final int price, final Category category, final Long brandId) {
        return new Product(id, category, Price.from(price), brandId);
    }

    public int getPriceValue() {
        return price.getValue();
    }

    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Price getPrice() {
        return price;
    }

    public Long getBrandId() {
        return brandId;
    }
}
