package com.movindu.pos.module.stockreceipt.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class StockReceiptRequest {

    private Long supplierId;

    @NotNull(message = "Received date is required")
    private LocalDateTime receivedDate;

    private String notes;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<StockReceiptItemRequest> items;
}