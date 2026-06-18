package com.movindu.pos.module.stockreceipt.entity;

import com.movindu.pos.common.base.BaseEntity;
import com.movindu.pos.common.enums.StockReceiptStatus;
import com.movindu.pos.module.supplier.entity.Supplier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stock_receipts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StockReceipt extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String receiptNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(nullable = false)
    private LocalDateTime receivedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockReceiptStatus status = StockReceiptStatus.DRAFT;

    @Column(length = 500)
    private String notes;

    @OneToMany(mappedBy = "stockReceipt",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<StockReceiptItem> items = new ArrayList<>();
}