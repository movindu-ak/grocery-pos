package com.movindu.pos.module.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponse {

    private SalesSummaryResponse salesSummary;
    private PaymentSummaryResponse paymentSummary;
    private List<TopProductResponse> topProducts;
    private List<LowStockResponse> lowStockProducts;
    private List<DailySalesDataResponse> last30DaysSales;
    private Long totalProducts;
    private Long totalCustomers;
    private Long totalSuppliers;
    private Long newCustomersThisMonth;
}