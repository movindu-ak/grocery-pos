package com.movindu.pos.module.sale.repository;

import com.movindu.pos.common.enums.PaymentMethod;
import com.movindu.pos.common.enums.SaleStatus;
import com.movindu.pos.module.sale.entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Optional<Sale> findByInvoiceNumber(String invoiceNumber);

    List<Sale> findByStatus(SaleStatus status);

    List<Sale> findByCustomerId(Long customerId);

    List<Sale> findBySaleDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.status = 'COMPLETED' AND s.saleDate >= :start AND s.saleDate <= :end")
    Long countCompletedSalesBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.status = 'COMPLETED' AND s.saleDate >= :start AND s.saleDate <= :end")
    BigDecimal sumRevenueBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s WHERE s.status = 'COMPLETED' AND s.paymentMethod = :method AND s.saleDate >= :start AND s.saleDate <= :end")
    BigDecimal sumRevenueByPaymentMethod(@Param("method") PaymentMethod method, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.status = 'COMPLETED' AND s.paymentMethod = :method AND s.saleDate >= :start AND s.saleDate <= :end")
    Long countByPaymentMethod(@Param("method") PaymentMethod method, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}