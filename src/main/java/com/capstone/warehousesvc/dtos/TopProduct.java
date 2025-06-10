package com.capstone.warehousesvc.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Top performing product by value")
public class TopProduct {

    @Schema(description = "Product unique identifier",
            example = "550e8400-e29b-41d4-a716-446655440000")
    private String productId;

    @Schema(description = "Product name",
            example = "Premium Coffee Beans")
    private String productName;

    @Schema(description = "Stock Keeping Unit identifier",
            example = "SKU-COFFEE-001")
    private String sku;

    @Schema(description = "Total inventory value for this product",
            example = "15000.00", minimum = "0")
    private BigDecimal value;

    @Schema(description = "Current stock quantity",
            example = "100", minimum = "0")
    private Integer quantity;
}