package com.movindu.pos.module.stockreceipt.dto.response;

import com.movindu.pos.common.enums.StockReceiptStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StockReceiptResponse {

    private Long id;
    private String receiptNumber;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime receivedDate;
    private StockReceiptStatus status;
    private String notes;
    private List<StockReceiptItemResponse> items;
    private LocalDateTime createdAt;
}