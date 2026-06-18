package com.movindu.pos.module.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DailySalesDataResponse {

    private LocalDate date;
    private Long salesCount;
    private BigDecimal revenue;
}