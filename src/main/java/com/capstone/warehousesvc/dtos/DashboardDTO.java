package com.capstone.warehousesvc.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Complete dashboard summary containing all metrics and analytics")
public class DashboardDTO {

    @Schema(description = "Inventory-related metrics and analytics", required = true)
    private InventoryMetrics inventoryMetrics;

    @Schema(description = "Transaction-related metrics and analytics", required = true)
    private TransactionMetrics transactionMetrics;

    @Schema(description = "Production-related metrics and analytics", required = true)
    private ProductionMetrics productionMetrics;

    @Schema(description = "List of recent activities in the system",
            example = "[{\"id\": \"123\", \"activityType\": \"SALE\", \"description\": \"Product sold\"}]")
    private List<RecentActivity> recentActivities;

    @Schema(description = "Various trend data and KPIs",
            example = "{\"salesGrowth\": 15.5, \"inventoryTurnover\": 2.3}")
    private Map<String, Object> trends;
}