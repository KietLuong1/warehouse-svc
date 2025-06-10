package com.capstone.warehousesvc.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Low stock alert information")
public class LowStockAlert {

    @Schema(description = "Product unique identifier",
            example = "550e8400-e29b-41d4-a716-446655440000")
    private String productId;

    @Schema(description = "Product name",
            example = "Office Supplies - Paper A4")
    private String productName;

    @Schema(description = "Stock Keeping Unit identifier",
            example = "SKU-PAPER-A4-001")
    private String sku;

    @Schema(description = "Current stock quantity",
            example = "5", minimum = "0")
    private Integer currentStock;

    @Schema(description = "Minimum threshold that triggered the alert",
            example = "10", minimum = "1")
    private Integer minimumThreshold;

    @Schema(description = "Alert severity level",
            example = "HIGH", allowableValues = {"LOW", "MEDIUM", "HIGH", "CRITICAL"})
    private String severity;
}