package com.example.musinsaserver.priceinformation.domain;

public class PriceInformation {
    private Long id;
    private Long productId;
    private Long brandId;
    private Long categoryId;
    private String category;
    private int price;
    private String brandName;

    private PriceInformation(
            final Long id,
            final Long productId,
            final Long brandId,
            final Long categoryId,
            final String category,
            final int price,
            final String brandName
    ) {
        this.id = id;
        this.productId = productId;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.category = category;
        this.price = price;
        this.brandName = brandName;
    }

    public static PriceInformation createWithoutId(
            final Long productId,
            final Long categoryId,
            final Long brandId,
            final String category,
            final int price,
            final String brandName
    ) {
        return new PriceInformation(null, productId, brandId, categoryId, category, price, brandName);
    }

    public static PriceInformation createWithId(
            final Long id,
            final Long productId,
            final Long categoryId,
            final Long brandId,
            final String category,
            final int price,
            final String brandName
    ) {
        return new PriceInformation(id, productId, brandId, categoryId, category, price, brandName);
    }

    public void update(final Long productId, final int price) {
        this.productId = productId;
        this.price = price;
    }

    public boolean isMoreExpensiveThan(final int comparedPrice) {
        return price > comparedPrice;
    }

    public boolean isCheaperThan(final int comparedPrice) {
        return price < comparedPrice;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getCategoryId() {
        return categoryId;
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
