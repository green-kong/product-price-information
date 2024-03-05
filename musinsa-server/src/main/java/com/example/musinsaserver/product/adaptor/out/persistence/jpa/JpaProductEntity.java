package com.example.musinsaserver.product.adaptor.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity(name = "products")
@Table(indexes =  @Index(name = "product_idx_brandId_categoryId", columnList = "brand_id, category_id"))
public class JpaProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "brand_id")
    private Long brandId;

    private int price;

    protected JpaProductEntity() {
    }

    public JpaProductEntity(
            final Long id,
            final Long categoryId,
            final int price,
            final Long brandId
    ) {
        this.id = id;
        this.categoryId = categoryId;
        this.price = price;
        this.brandId = brandId;
    }

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public int getPrice() {
        return price;
    }

    public Long getBrandId() {
        return brandId;
    }
}
