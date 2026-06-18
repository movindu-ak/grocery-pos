package com.movindu.pos.module.customer.controller;

import com.movindu.pos.common.response.ApiResponse;
import com.movindu.pos.module.customer.dto.request.CustomerRequest;
import com.movindu.pos.module.customer.dto.response.CustomerResponse;
import com.movindu.pos.module.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully",
                        customerService.createCustomer(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerById(
            @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(
                customerService.getCustomerById(id)));
    }

    @GetMapping("/phone/{phone}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomerByPhone(
            @PathVariable String phone) {
        return ResponseEntity.ok(ApiResponse.success(
                customerService.getCustomerByPhone(phone)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAllCustomers() {
        return ResponseEntity.ok(ApiResponse.success(
                customerService.getAllCustomers()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Long id, @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully",
                customerService.updateCustomer(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(ApiResponse.success(
                "Customer deleted successfully", null));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> searchCustomers(
            @RequestParam String name) {
        return ResponseEntity.ok(ApiResponse.success(
                customerService.searchCustomers(name)));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getActiveCustomers() {
        return ResponseEntity.ok(ApiResponse.success(
                customerService.getActiveCustomers()));
    }
}