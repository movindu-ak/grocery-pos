package com.movindu.pos.module.inventory.service;

import com.movindu.pos.module.inventory.dto.response.InventoryResponse;
import com.movindu.pos.module.inventory.entity.Inventory;
import com.movindu.pos.module.inventory.repository.InventoryRepository;
import com.movindu.pos.module.product.entity.Product;
import com.movindu.pos.module.stockreceipt.repository.StockReceiptItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final StockReceiptItemRepository stockReceiptItemRepository;

    @Transactional
    public void increaseStock(Product product, int quantity) {
        Inventory inventory = inventoryRepository
                .findByProductId(product.getId())
                .orElseGet(() -> {
                    Inventory inv = new Inventory();
                    inv.setProduct(product);
                    inv.setCurrentStock(0);
                    return inv;
                });
        inventory.setCurrentStock(inventory.getCurrentStock() + quantity);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void decreaseStock(Product product, int quantity) {
        Inventory inventory = inventoryRepository
                .findByProductId(product.getId())
                .orElseGet(() -> {
                    Inventory inv = new Inventory();
                    inv.setProduct(product);
                    inv.setCurrentStock(product.getStockQuantity());
                    return inv;
                });
        int newStock = Math.max(0, inventory.getCurrentStock() - quantity);
        inventory.setCurrentStock(newStock);
        inventory.setLastUpdated(LocalDateTime.now());
        inventoryRepository.save(inventory);
    }

    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAllWithProduct()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InventoryResponse mapToResponse(Inventory inv) {
        Product product = inv.getProduct();
        InventoryResponse response = new InventoryResponse();
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setSku(product.getSku());
        response.setBarcode(product.getBarcode());
        response.setCategoryName(
                product.getCategory() != null
                        ? product.getCategory().getName() : null);
        response.setCurrentStock(inv.getCurrentStock());
        response.setMinimumStock(product.getAverageThreshold());
        response.setCurrentSellingPrice(product.getPrice());
        response.setLastUpdated(inv.getLastUpdated());
        response.setStockStatus(
            getStockStatus(inv.getCurrentStock(), 
                product.getAverageThreshold(), 
                product.getGoodThreshold()));
        response.setAverageCostPrice(
                calculateAverageCost(product.getId()));
        return response;
    }

    private String getStockStatus(int current, int averageThreshold, int goodThreshold) {
    if (current <= 0) return "BAD";
    if (current <= averageThreshold) return "BAD";
    if (current <= goodThreshold) return "AVERAGE";
    return "GOOD";
}
    private BigDecimal calculateAverageCost(Long productId) {
        List<Object[]> costData = stockReceiptItemRepository
                .findCostDataByProductId(productId);
        if (costData.isEmpty()) return BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;
        int totalQty = 0;
        for (Object[] row : costData) {
            Integer qty = (Integer) row[0];
            BigDecimal cost = (BigDecimal) row[1];
            totalCost = totalCost.add(cost.multiply(BigDecimal.valueOf(qty)));
            totalQty += qty;
        }
        if (totalQty == 0) return BigDecimal.ZERO;
        return totalCost.divide(
                BigDecimal.valueOf(totalQty), 2, RoundingMode.HALF_UP);
    }
}