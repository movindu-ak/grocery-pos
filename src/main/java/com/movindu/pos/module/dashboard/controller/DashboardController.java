package com.movindu.pos.module.dashboard.controller;

import com.movindu.pos.common.response.ApiResponse;
import com.movindu.pos.module.dashboard.dto.response.DailySalesDataResponse;
import com.movindu.pos.module.dashboard.dto.response.DashboardResponse;
import com.movindu.pos.module.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    // @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {
        return ResponseEntity.ok(ApiResponse.success(
                dashboardService.getDashboardSummary()));
    }

    @GetMapping("/sales/last-30-days")
    // @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<DailySalesDataResponse>>> getLast30Days() {
        return ResponseEntity.ok(ApiResponse.success(
                dashboardService.getLast30DaysSales()));
    }

    @GetMapping("/sales/last-7-days")
    // @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<DailySalesDataResponse>>> getLast7Days() {
        return ResponseEntity.ok(ApiResponse.success(
                dashboardService.getLast7DaysSales()));
    }
}