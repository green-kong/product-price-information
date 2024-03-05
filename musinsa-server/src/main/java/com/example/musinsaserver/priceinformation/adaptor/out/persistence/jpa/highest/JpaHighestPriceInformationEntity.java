package com.example.musinsaserver.priceinformation.adaptor.out.persistence.jpa.highest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "highest_price_informations")
public class JpaHighestPriceInformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private Long brandId;
    private Long categoryId;
    private String category;
    private int price;
    private String brandName;

    protected JpaHighestPriceInformationEntity() {
    }

    public JpaHighestPriceInformationEntity(
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
    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getBrandId() {
        return brandId;
    }

    public Long getCategoryId() {
        return categoryId;
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
