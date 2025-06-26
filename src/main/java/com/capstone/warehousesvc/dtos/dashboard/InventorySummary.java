package com.capstone.warehousesvc.dtos.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Summary of inventory analytics and insights")
public class InventorySummary {

    @Schema(description = "Total number of inventory records")
    private Long totalInventoryItems;

    @Schema(description = "Total value of all inventory")
    private BigDecimal totalInventoryValue;

    @Schema(description = "Number of low stock items")
    private Long lowStockCount;

    @Schema(description = "Number of overstock items")
    private Long overstockCount;

    @Schema(description = "Number of out of stock items")
    private Long outOfStockCount;

    @Schema(description = "Number of items expiring soon")
    private Long expiringSoonCount;

    @Schema(description = "Top products by value")
    private List<TopInventoryItem> topValueItems;

    @Schema(description = "Items requiring immediate attention")
    private List<InventoryAlert> alerts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Top inventory item by value")
    public static class TopInventoryItem {
        
        @Schema(description = "Product name")
        private String productName;
        
        @Schema(description = "Warehouse name")
        private String warehouseName;
        
        @Schema(description = "Quantity on hand")
        private Integer quantity;
        
        @Schema(description = "Total value")
        private BigDecimal totalValue;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "Inventory alert information")
    public static class InventoryAlert {
        
        @Schema(description = "Alert type", example = "LOW_STOCK")
        private String alertType;
        
        @Schema(description = "Product name")
        private String productName;
        
        @Schema(description = "Warehouse name")
        private String warehouseName;
        
        @Schema(description = "Current quantity")
        private Integer currentQuantity;
        
        @Schema(description = "Alert threshold")
        private Integer threshold;
        
        @Schema(description = "Alert message")
        private String message;
    }
}
