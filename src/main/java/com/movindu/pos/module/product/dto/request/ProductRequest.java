package com.movindu.pos.module.product.dto.request;

import com.movindu.pos.common.enums.DiscountType;
import com.movindu.pos.common.enums.ProductStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    @NotBlank(message = "SKU is required")
    private String sku;

    @NotBlank(message = "Barcode is required")
    private String barcode;

    private Long categoryId;

    // stock level thresholds
    @Min(value = 0, message = "Good threshold cannot be negative")
    private Integer goodThreshold = 50;

    @Min(value = 0, message = "Average threshold cannot be negative")
    private Integer averageThreshold = 20;

    // discount
    private DiscountType discountType = DiscountType.NONE;

    @DecimalMin(value = "0.0", message = "Discount value cannot be negative")
    private BigDecimal discountValue = BigDecimal.ZERO;

    // status
    private ProductStatus status = ProductStatus.ACTIVE;
}