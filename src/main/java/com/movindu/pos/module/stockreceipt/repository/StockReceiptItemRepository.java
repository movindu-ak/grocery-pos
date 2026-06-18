package com.movindu.pos.module.stockreceipt.repository;

import com.movindu.pos.module.stockreceipt.entity.StockReceiptItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockReceiptItemRepository extends JpaRepository<StockReceiptItem, Long> {

    List<StockReceiptItem> findByStockReceiptId(Long stockReceiptId);

    List<StockReceiptItem> findByProductId(Long productId);
}