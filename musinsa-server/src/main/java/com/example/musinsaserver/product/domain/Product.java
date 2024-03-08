package com.example.musinsaserver.product.domain;

public class Product {

    private Long id;
    private Long categoryId;
    private Price price;
    private Long brandId;

    private Product(final Long id, final Long categoryId, final Price price, final Long brandId) {
        this.id = id;
        this.categoryId = categoryId;
        this.price = price;
        this.brandId = brandId;
    }

    public static Product createWithoutId(final int price, final Long categoryId, final Long brandId) {
        return new Product(null, categoryId, Price.from(price), brandId);
    }

    public static Product createWithId(final Long id, final int price, final Long categoryId, final Long brandId) {
        return new Product(id, categoryId, Price.from(price), brandId);
    }

    public void update(final int price) {
        this.price = Price.from(price);
    }

    public boolean belongsToSameBrand(final Long brandId) {
        return brandId.equals(this.brandId);
    }

    public int getPriceValue() {
        return price.getValue();
    }

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getBrandId() {
        return brandId;
    }
}
