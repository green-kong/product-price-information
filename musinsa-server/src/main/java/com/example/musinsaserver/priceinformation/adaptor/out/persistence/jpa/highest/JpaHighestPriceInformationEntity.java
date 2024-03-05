package com.example.musinsaserver.priceinformation.adaptor.out.persistence.jpa.highest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity(name = "highest_price_informations")
@Table(indexes = {
        @Index(name = "highest_idx_category_id", columnList = "category_id"),
        @Index(name = "highest_idx_brand_id", columnList = "brand_id")
})
public class JpaHighestPriceInformationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "brand_name")
    private String brandName;
    private String category;
    private int price;


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
