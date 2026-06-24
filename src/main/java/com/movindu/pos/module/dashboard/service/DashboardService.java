package com.movindu.pos.module.dashboard.service;

import com.movindu.pos.module.dashboard.dto.response.DailySalesDataResponse;
import com.movindu.pos.module.dashboard.dto.response.DashboardResponse;

import java.util.List;

public interface DashboardService {

    DashboardResponse getDashboardSummary();

    List<DailySalesDataResponse> getLast30DaysSales();

    List<DailySalesDataResponse> getLast7DaysSales();
}