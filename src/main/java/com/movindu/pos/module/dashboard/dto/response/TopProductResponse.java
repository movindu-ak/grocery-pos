package com.movindu.pos.module.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopProductResponse {

    private Long productId;
    private String productName;
    private String productSku;
    private String barcode;
    private Long totalQuantitySold;
    private BigDecimal totalRevenue;
}