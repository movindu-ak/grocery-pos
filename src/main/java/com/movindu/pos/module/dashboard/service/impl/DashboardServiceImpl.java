package com.movindu.pos.module.dashboard.service.impl;

import com.movindu.pos.common.enums.PaymentMethod;
import com.movindu.pos.common.enums.ProductStatus;
import com.movindu.pos.common.enums.SaleStatus;
import com.movindu.pos.module.customer.repository.CustomerRepository;
import com.movindu.pos.module.dashboard.dto.response.*;
import com.movindu.pos.module.dashboard.service.DashboardService;
import com.movindu.pos.module.product.entity.Product;
import com.movindu.pos.module.product.repository.ProductRepository;
import com.movindu.pos.module.sale.repository.SaleRepository;
import com.movindu.pos.module.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public DashboardResponse getDashboardSummary() {

        LocalDateTime todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime todayEnd = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        LocalDateTime weekStart = LocalDateTime.of(
                LocalDate.now().minusDays(7), LocalTime.MIN);
        LocalDateTime monthStart = LocalDateTime.of(
                LocalDate.now().minusDays(30), LocalTime.MIN);
        LocalDateTime now = LocalDateTime.now();

        SalesSummaryResponse salesSummary = new SalesSummaryResponse(
                saleRepository.countCompletedSalesBetween(todayStart, todayEnd),
                saleRepository.countCompletedSalesBetween(weekStart, now),
                saleRepository.countCompletedSalesBetween(monthStart, now),
                saleRepository.sumRevenueBetween(todayStart, todayEnd),
                saleRepository.sumRevenueBetween(weekStart, now),
                saleRepository.sumRevenueBetween(monthStart, now),
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO
        );

        PaymentSummaryResponse paymentSummary = buildPaymentSummary(
                monthStart, todayEnd);

        List<TopProductResponse> topProducts = getTopProducts();

        List<LowStockResponse> lowStock = getLowStockProducts();

        List<DailySalesDataResponse> last30Days = getLast30DaysSales();

        Long totalProducts = productRepository.countByStatus(ProductStatus.ACTIVE);
        Long totalCustomers = customerRepository.count();
        Long totalSuppliers = supplierRepository.count();

        LocalDateTime monthStartDate = LocalDateTime.of(
                LocalDate.now().withDayOfMonth(1), LocalTime.MIN);
        Long newCustomersThisMonth = customerRepository
                .countByCreatedAtAfter(monthStartDate);

        return new DashboardResponse(
                salesSummary,
                paymentSummary,
                topProducts,
                lowStock,
                last30Days,
                totalProducts,
                totalCustomers,
                totalSuppliers,
                newCustomersThisMonth
        );
    }

    private PaymentSummaryResponse buildPaymentSummary(
            LocalDateTime start, LocalDateTime end) {

        BigDecimal cashTotal = saleRepository.sumRevenueByPaymentMethod(
                PaymentMethod.CASH, start, end);
        Long cashCount = saleRepository.countByPaymentMethod(
                PaymentMethod.CASH, start, end);

        BigDecimal cardTotal = saleRepository.sumRevenueByPaymentMethod(
                PaymentMethod.CARD, start, end);
        Long cardCount = saleRepository.countByPaymentMethod(
                PaymentMethod.CARD, start, end);

        BigDecimal splitTotal = saleRepository.sumRevenueByPaymentMethod(
                PaymentMethod.SPLIT, start, end);
        Long splitCount = saleRepository.countByPaymentMethod(
                PaymentMethod.SPLIT, start, end);

        BigDecimal grandTotal = cashTotal.add(cardTotal).add(splitTotal);
        Long totalTransactions = cashCount + cardCount + splitCount;

        return new PaymentSummaryResponse(
                cashTotal, cashCount,
                cardTotal, cardCount,
                splitTotal, splitCount,
                grandTotal, totalTransactions
        );
    }

    private List<TopProductResponse> getTopProducts() {
        List<Object[]> results = saleRepository.findTopSellingProducts();
        List<TopProductResponse> topProducts = new ArrayList<>();

        for (Object[] row : results) {
            TopProductResponse response = new TopProductResponse();
            response.setProductId(((Number) row[0]).longValue());
            response.setProductName((String) row[1]);
            response.setProductSku((String) row[2]);
            response.setBarcode((String) row[3]);
            response.setTotalQuantitySold(((Number) row[4]).longValue());
            response.setTotalRevenue((BigDecimal) row[5]);
            topProducts.add(response);
        }

        return topProducts.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    private List<LowStockResponse> getLowStockProducts() {
        List<Product> products = productRepository
                .findByStockQuantityLessThanEqualAndStatus(10, ProductStatus.ACTIVE);

        return products.stream()
                .map(p -> new LowStockResponse(
                        p.getId(),
                        p.getName(),
                        p.getSku(),
                        p.getBarcode(),
                        p.getStockQuantity(),
                        10,
                        p.getCategory() != null ? p.getCategory().getName() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DailySalesDataResponse> getLast30DaysSales() {
        return getDailySalesData(30);
    }

    @Override
    public List<DailySalesDataResponse> getLast7DaysSales() {
        return getDailySalesData(7);
    }

    private List<DailySalesDataResponse> getDailySalesData(int days) {
        List<DailySalesDataResponse> result = new ArrayList<>();

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);

            Long count = saleRepository.countCompletedSalesBetween(start, end);
            BigDecimal revenue = saleRepository.sumRevenueBetween(start, end);

            result.add(new DailySalesDataResponse(date, count, revenue));
        }

        return result;
    }
}