package com.movindu.pos.module.stockreceipt.repository;

import com.movindu.pos.module.stockreceipt.entity.StockReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface StockReceiptItemRepository extends JpaRepository<StockReceiptItem, Long> {

    List<StockReceiptItem> findByStockReceiptId(Long stockReceiptId);

    List<StockReceiptItem> findByProductId(Long productId);

     @Query("SELECT si.quantity, si.costPrice " +
           "FROM StockReceiptItem si " +
           "WHERE si.product.id = :productId " +
           "AND si.stockReceipt.status = 'CONFIRMED'")
    List<Object[]> findCostDataByProductId(@Param("productId") Long productId);
}