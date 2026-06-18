package com.movindu.pos.module.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSummaryResponse {

    private BigDecimal cashTotal;
    private Long cashCount;

    private BigDecimal cardTotal;
    private Long cardCount;

    private BigDecimal splitTotal;
    private Long splitCount;

    private BigDecimal grandTotal;
    private Long totalTransactions;
}