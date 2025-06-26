
package com.capstone.warehousesvc.dtos.dashboard;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Production and supplier analytics")
public class ProductionMetrics {

    @Schema(description = "Total number of products produced/received",
            example = "500", minimum = "0")
    private Integer totalProductsProduced;

    @Schema(description = "Number of active suppliers",
            example = "25", minimum = "0")
    private Integer activeSuppliers;

    @Schema(description = "Total value of production/procurement",
            example = "75000.00", minimum = "0")
    private BigDecimal productionValue;

    @Schema(description = "Production efficiency percentage",
            example = "85.5", minimum = "0", maximum = "100")
    private Double productionEfficiency;

    @Schema(description = "Performance metrics by product category")
    private List<CategoryPerformance> categoryPerformance;

    @Schema(description = "Monthly production data",
            example = "{\"January\": 120, \"February\": 135}")
    private Map<String, Integer> monthlyProduction;
}