package com.movindu.pos.module.customer.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CustomerResponse {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private Integer loyaltyPoints;
    private BigDecimal creditLimit;
    private BigDecimal outstandingBalance;
    private BigDecimal totalPurchases;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}