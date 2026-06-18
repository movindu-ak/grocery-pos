package com.movindu.pos.module.sale.controller;

import com.movindu.pos.common.response.ApiResponse;
import com.movindu.pos.module.sale.dto.request.SaleRequest;
import com.movindu.pos.module.sale.dto.response.SaleResponse;
import com.movindu.pos.module.sale.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ApiResponse<SaleResponse>> createSale(
            @Valid @RequestBody SaleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sale completed successfully",
                        saleService.createSale(request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ApiResponse<SaleResponse>> getSaleById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                saleService.getSaleById(id)));
    }

    @GetMapping("/invoice/{invoiceNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ApiResponse<SaleResponse>> getSaleByInvoiceNumber(
            @PathVariable String invoiceNumber) {
        return ResponseEntity.ok(ApiResponse.success(
                saleService.getSaleByInvoiceNumber(invoiceNumber)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<SaleResponse>>> getAllSales() {
        return ResponseEntity.ok(ApiResponse.success(
                saleService.getAllSales()));
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'CASHIER')")
    public ResponseEntity<ApiResponse<List<SaleResponse>>> getSalesByCustomer(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(ApiResponse.success(
                saleService.getSalesByCustomer(customerId)));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SaleResponse>> cancelSale(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                "Sale cancelled successfully",
                saleService.cancelSale(id)));
    }
}