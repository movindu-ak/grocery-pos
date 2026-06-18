package com.movindu.pos.module.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesSummaryResponse {

    private Long todayCount;
    private Long weeklyCount;
    private Long monthlyCount;

    private BigDecimal todayRevenue;
    private BigDecimal weeklyRevenue;
    private BigDecimal monthlyRevenue;

    private BigDecimal todayProfit;
    private BigDecimal weeklyProfit;
    private BigDecimal monthlyProfit;
}