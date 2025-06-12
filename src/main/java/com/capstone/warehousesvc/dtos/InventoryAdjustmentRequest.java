package com.capstone.warehousesvc.dtos;

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
@Schema(description = "Request for inventory quantity adjustment")
public class InventoryAdjustmentRequest {

    @Schema(description = "Inventory ID", example = "123e4567-e89b-12d3-a456-426614174000", required = true)
    @NotBlank(message = "Inventory ID is required")
    private String inventoryId;

    @Schema(description = "Adjustment type", example = "ADD", allowableValues = {"ADD", "SUBTRACT", "SET"}, required = true)
    @NotBlank(message = "Adjustment type is required")
    private String adjustmentType;

    @Schema(description = "Quantity to adjust", example = "50", required = true)
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @Schema(description = "Reason for adjustment", example = "Damaged goods removal")
    private String reason;

    @Schema(description = "Reference number", example = "ADJ-2024-001")
    private String referenceNumber;

    @Schema(description = "Notes or additional comments", example = "Found damaged during inspection")
    private String notes;
}
