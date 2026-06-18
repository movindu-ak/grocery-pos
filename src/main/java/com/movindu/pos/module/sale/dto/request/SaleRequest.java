package com.movindu.pos.module.sale.dto.request;

import com.movindu.pos.common.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SaleRequest {

    private Long customerId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Paid amount is required")
    private BigDecimal paidAmount;

    private BigDecimal cashAmount = BigDecimal.ZERO;

    private BigDecimal cardAmount = BigDecimal.ZERO;

    private BigDecimal discountAmount = BigDecimal.ZERO;

    private String notes;

    @NotEmpty(message = "At least one item is required")
    @Valid
    private List<SaleItemRequest> items;
}