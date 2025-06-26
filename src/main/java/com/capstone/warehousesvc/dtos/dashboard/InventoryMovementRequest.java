package com.capstone.warehousesvc.dtos.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Inventory movement request for transferring stock between warehouses")
public class InventoryMovementRequest {

    @Schema(description = "Source warehouse ID", example = "123e4567-e89b-12d3-a456-426614174001", required = true)
    private String fromWarehouseId;

    @Schema(description = "Destination warehouse ID", example = "123e4567-e89b-12d3-a456-426614174002", required = true)
    private String toWarehouseId;

    @Schema(description = "Product ID", example = "123e4567-e89b-12d3-a456-426614174003", required = true)
    private String productId;

    @Schema(description = "Quantity to transfer", example = "25", required = true)
    private Integer quantity;

    @Schema(description = "Movement reason", example = "Rebalancing stock levels")
    private String reason;

    @Schema(description = "Reference number", example = "MOV-2024-001")
    private String referenceNumber;

    @Schema(description = "Notes or additional comments", example = "Urgent transfer for high-demand location")
    private String notes;
}
