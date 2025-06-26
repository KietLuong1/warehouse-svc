package com.capstone.warehousesvc.dtos.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Comprehensive inventory metrics and analytics")
public class InventoryMetrics {

    @Schema(description = "Total number of products in inventory",
            example = "1250", minimum = "0")
    private Integer totalProducts;

    @Schema(description = "Number of products with low stock levels",
            example = "25", minimum = "0")
    private Integer lowStockProducts;

    @Schema(description = "Number of products that are out of stock",
            example = "5", minimum = "0")
    private Integer outOfStockProducts;

    @Schema(description = "Total monetary value of all inventory",
            example = "125000.50", minimum = "0")
    private BigDecimal totalInventoryValue;

    @Schema(description = "Number of products expiring within warning period",
            example = "15", minimum = "0")
    private Integer expiringSoon;

    @Schema(description = "Average stock level across all products",
            example = "45.5", minimum = "0")
    private Double averageStockLevel;

    @Schema(description = "List of top products by inventory value")
    private List<TopProduct> topProductsByValue;

    @Schema(description = "List of low stock alerts requiring attention")
    private List<LowStockAlert> lowStockAlerts;
}