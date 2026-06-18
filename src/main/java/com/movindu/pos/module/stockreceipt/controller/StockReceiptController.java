package com.movindu.pos.module.stockreceipt.controller;

import com.movindu.pos.common.response.ApiResponse;
import com.movindu.pos.module.stockreceipt.dto.request.StockReceiptRequest;
import com.movindu.pos.module.stockreceipt.dto.response.StockReceiptResponse;
import com.movindu.pos.module.stockreceipt.service.StockReceiptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-receipts")
@RequiredArgsConstructor
public class StockReceiptController {

    private final StockReceiptService stockReceiptService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STOREKEEPER')")
    public ResponseEntity<ApiResponse<StockReceiptResponse>> createReceipt(
            @Valid @RequestBody StockReceiptRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Stock receipt created successfully",
                        stockReceiptService.createReceipt(request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STOREKEEPER')")
    public ResponseEntity<ApiResponse<StockReceiptResponse>> getReceiptById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                stockReceiptService.getReceiptById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STOREKEEPER')")
    public ResponseEntity<ApiResponse<List<StockReceiptResponse>>> getAllReceipts() {
        return ResponseEntity.ok(ApiResponse.success(
                stockReceiptService.getAllReceipts()));
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<StockReceiptResponse>> confirmReceipt(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Stock receipt confirmed — stock updated",
                stockReceiptService.confirmReceipt(id)));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<StockReceiptResponse>> cancelReceipt(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Stock receipt cancelled",
                stockReceiptService.cancelReceipt(id)));
    }

    @GetMapping("/supplier/{supplierId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STOREKEEPER')")
    public ResponseEntity<ApiResponse<List<StockReceiptResponse>>> getReceiptsBySupplier(
            @PathVariable Long supplierId) {
        return ResponseEntity.ok(ApiResponse.success(
                stockReceiptService.getReceiptsBySupplier(supplierId)));
    }
}