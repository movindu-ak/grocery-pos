package com.movindu.pos.module.product.dto.response;

import com.movindu.pos.common.enums.DiscountType;
import com.movindu.pos.common.enums.ProductStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String sku;
    private String barcode;

    // stock thresholds
    private Integer goodThreshold;
    private Integer averageThreshold;
    private String stockLevel;   // GOOD / AVERAGE / BAD

    // discount
    private DiscountType discountType;
    private BigDecimal discountValue;
    private BigDecimal effectivePrice;  // price after discount

    private ProductStatus status;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}