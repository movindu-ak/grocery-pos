package com.movindu.pos.module.supplier.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplierResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String contactPerson;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}