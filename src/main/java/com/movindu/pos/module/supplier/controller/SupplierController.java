package com.movindu.pos.module.supplier.controller;

import com.movindu.pos.common.response.ApiResponse;
import com.movindu.pos.module.supplier.dto.request.SupplierRequest;
import com.movindu.pos.module.supplier.dto.response.SupplierResponse;
import com.movindu.pos.module.supplier.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(
            @Valid @RequestBody SupplierRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Supplier created successfully",
                        supplierService.createSupplier(request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STOREKEEPER')")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                supplierService.getSupplierById(id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STOREKEEPER')")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getAllSuppliers() {
        return ResponseEntity.ok(ApiResponse.success(
                supplierService.getAllSuppliers()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplier(
            @PathVariable Long id, @Valid @RequestBody SupplierRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Supplier updated successfully",
                supplierService.updateSupplier(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success(
                "Supplier deleted successfully", null));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STOREKEEPER')")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getActiveSuppliers() {
        return ResponseEntity.ok(ApiResponse.success(
                supplierService.getActiveSuppliers()));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'STOREKEEPER')")
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> searchSuppliers(
            @RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(
                supplierService.searchSuppliers(name)));
    }
}
