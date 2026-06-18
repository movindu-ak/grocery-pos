package com.movindu.pos.module.stockreceipt.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StockReceiptItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String productSku;
    private Integer quantity;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private String batchNumber;
    private LocalDate expiryDate;
}