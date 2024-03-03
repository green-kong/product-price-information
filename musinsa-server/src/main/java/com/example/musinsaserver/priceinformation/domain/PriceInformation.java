package com.example.musinsaserver.priceinformation.domain;

public class PriceInformation {
    private Long id;
    private Long productId;
    private Long brandId;
    private String category;
    private int price;
    private String brandName;

    private PriceInformation(
            final Long id,
            final Long productId,
            final Long brandId,
            final String category,
            final int price,
            final String brandName
    ) {
        this.id = id;
        this.productId = productId;
        this.brandId = brandId;
        this.category = category;
        this.price = price;
        this.brandName = brandName;
    }

    public static PriceInformation createWithoutId(
            final Long productId,
            final Long brandId,
            final String category,
            final int price,
            final String brandName
    ) {
        return new PriceInformation(null, productId, brandId, category, price, brandName);
    }

    public static PriceInformation createWithId(
            final Long id,
            final Long productId,
            final Long brandId,
            final String category,
            final int price,
            final String brandName
    ) {
        return new PriceInformation(id, productId, brandId, category, price, brandName);
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public String getCategory() {
        return category;
    }

    public int getPrice() {
        return price;
    }

    public String getBrandName() {
        return brandName;
    }
}
