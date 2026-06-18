package com.movindu.pos.module.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LowStockResponse {

    private Long productId;
    private String productName;
    private String productSku;
    private String barcode;
    private Integer currentStock;
    private Integer minimumStock;
    private String categoryName;
}