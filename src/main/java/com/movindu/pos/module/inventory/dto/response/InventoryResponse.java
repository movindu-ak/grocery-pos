package com.movindu.pos.module.inventory.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {

    private Long productId;
    private String productName;
    private String sku;
    private String barcode;
    private String categoryName;
    private Integer currentStock;
    private Integer minimumStock;
    private String stockStatus;
    private BigDecimal currentSellingPrice;
    private BigDecimal averageCostPrice;
    private LocalDateTime lastUpdated;
}