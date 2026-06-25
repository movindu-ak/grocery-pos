package com.movindu.pos.module.inventory.repository;

import com.movindu.pos.module.inventory.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(Long productId);

    @Query("SELECT i FROM Inventory i JOIN FETCH i.product p " +
           "LEFT JOIN FETCH p.category " +
           "ORDER BY p.name ASC")
    List<Inventory> findAllWithProduct();

    @Query("SELECT si.quantity, si.costPrice " +
       "FROM StockReceiptItem si " +
       "WHERE si.product.id = :productId " +
       "AND si.stockReceipt.status = 'CONFIRMED'")
List<Object[]> findCostDataByProductId(@Param("productId") Long productId);
}