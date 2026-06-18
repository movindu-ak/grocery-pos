 package com.movindu.pos.module.sale.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SaleItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String productSku;
    private String barcode;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal totalPrice;
}