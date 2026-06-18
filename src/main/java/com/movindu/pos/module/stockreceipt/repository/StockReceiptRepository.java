package com.movindu.pos.module.stockreceipt.repository;

import com.movindu.pos.common.enums.StockReceiptStatus;
import com.movindu.pos.module.stockreceipt.entity.StockReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockReceiptRepository extends JpaRepository<StockReceipt, Long> {

    Optional<StockReceipt> findByReceiptNumber(String receiptNumber);

    boolean existsByReceiptNumber(String receiptNumber);

    List<StockReceipt> findByStatus(StockReceiptStatus status);

    List<StockReceipt> findBySupplierId(Long supplierId);
}