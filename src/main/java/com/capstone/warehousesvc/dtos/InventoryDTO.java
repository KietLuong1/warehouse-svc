package com.capstone.warehousesvc.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Inventory details")
public class InventoryDTO {

    @Schema(description = "Inventory ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Product ID", example = "123e4567-e89b-12d3-a456-426614174001", required = true)
    @NotBlank(message = "Product ID is required")
    private String productId;

    @Schema(description = "Warehouse ID", example = "123e4567-e89b-12d3-a456-426614174002", required = true)
    @NotBlank(message = "Warehouse ID is required")
    private String warehouseId;

    @Schema(description = "Current quantity available", example = "100", required = true)
    @NotNull(message = "Quantity on hand is required")
    @Min(value = 0, message = "Quantity on hand cannot be negative")
    private Integer quantityOnHand;

    @Schema(description = "Quantity reserved for orders", example = "10")
    @Min(value = 0, message = "Reserved quantity cannot be negative")
    private Integer reservedQuantity;

    @Schema(description = "Minimum stock level before reorder", example = "20")
    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;

    @Schema(description = "Maximum stock level", example = "500")
    @Min(value = 0, message = "Max stock level cannot be negative")
    private Integer maxStockLevel;

    @Schema(description = "Unit cost of the item", example = "25.50")
    private BigDecimal unitCost;

    @Schema(description = "Storage location code", example = "A1-B2-C3")
    private String locationCode;

    @Schema(description = "Batch or lot number", example = "BATCH2024001")
    private String batchNumber;

    @Schema(description = "Expiry date for perishable items")
    private LocalDateTime expiryDate;

    @Schema(description = "Last physical count date")
    private LocalDateTime lastCountedDate;

    @Schema(description = "Last update timestamp")
    private LocalDateTime lastUpdated;

    @Schema(description = "User who last updated this record")
    private String updatedBy;

    // Product details (populated when fetching inventory with product details)
    @Schema(description = "Product details")
    private ProductDTO product;

    // Warehouse details (populated when fetching inventory with warehouse details)
    @Schema(description = "Warehouse details")
    private WarehouseDTO warehouse;

    // Computed fields
    @Schema(description = "Available quantity (on hand - reserved)", example = "90")
    private Integer availableQuantity;

    @Schema(description = "Whether stock is below reorder level")
    private Boolean isLowStock;

    @Schema(description = "Whether stock exceeds maximum level")
    private Boolean isOverstock;

    @Schema(description = "Total value of inventory (quantity * unit cost)", example = "2550.00")
    private BigDecimal totalValue;
}
