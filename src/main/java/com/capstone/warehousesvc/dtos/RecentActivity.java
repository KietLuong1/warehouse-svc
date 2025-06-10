package com.capstone.warehousesvc.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Recent system activity record")
public class RecentActivity {

    @Schema(description = "Unique identifier for the activity",
            example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;

    @Schema(description = "Type of activity performed",
            example = "SALE", allowableValues = {"SALE", "PURCHASE", "RETURN", "INVENTORY_UPDATE"})
    private String activityType;

    @Schema(description = "Detailed description of the activity",
            example = "Product XYZ sold to customer ABC")
    private String description;

    @Schema(description = "ID of the user who performed the activity",
            example = "user123")
    private String userId;

    @Schema(description = "Username of the person who performed the activity",
            example = "john.doe")
    private String username;

    @Schema(description = "Timestamp when the activity occurred",
            example = "2024-01-15T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Current status of the activity",
            example = "COMPLETED", allowableValues = {"PENDING", "PROCESSING", "COMPLETED", "FAILED"})
    private String status;
}