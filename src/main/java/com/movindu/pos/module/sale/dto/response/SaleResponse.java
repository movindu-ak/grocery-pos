package com.movindu.pos.module.sale.dto.response;

import com.movindu.pos.common.enums.PaymentMethod;
import com.movindu.pos.common.enums.SaleStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SaleResponse {

    private Long id;
    private String invoiceNumber;
    private Long customerId;
    private String customerName;
    private String customerPhone;
    private LocalDateTime saleDate;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal changeAmount;
    private PaymentMethod paymentMethod;
    private BigDecimal cashAmount;
    private BigDecimal cardAmount;
    private SaleStatus status;
    private String notes;
    private Integer loyaltyPointsEarned;
    private List<SaleItemResponse> items;
    private LocalDateTime createdAt;
}
