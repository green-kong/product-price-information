package com.example.musinsaserver.product.adaptor.out.persistence.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "products")
public class JpaProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long categoryId;
    private int price;
    private Long brandId;

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
