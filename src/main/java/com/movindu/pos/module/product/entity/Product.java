package com.movindu.pos.module.product.entity;

import com.movindu.pos.common.base.BaseEntity;
import com.movindu.pos.common.enums.DiscountType;
import com.movindu.pos.common.enums.ProductStatus;
import com.movindu.pos.module.category.entity.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Column(unique = true)
    private String sku;

    @Column(unique = true)
    private String barcode;

    // stock level thresholds (Option B - custom per product)
    @Column(nullable = false)
    private Integer goodThreshold = 50;

    @Column(nullable = false)
    private Integer averageThreshold = 20;

    // discount
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DiscountType discountType = DiscountType.NONE;

    @Column(nullable = false)
    private BigDecimal discountValue = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
}