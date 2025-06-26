package com.capstone.warehousesvc.dtos.dashboard;

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
@Schema(description = "Performance metrics for a product category")
public class CategoryPerformance {

    @Schema(description = "Category unique identifier",
            example = "550e8400-e29b-41d4-a716-446655440000")
    private String categoryId;

    @Schema(description = "Category name",
            example = "Electronics")
    private String categoryName;

    @Schema(description = "Number of products in this category",
            example = "150", minimum = "0")
    private Integer productCount;

    @Schema(description = "Total inventory value for the category",
            example = "25000.00", minimum = "0")
    private BigDecimal totalValue;

    @Schema(description = "Performance score based on various metrics",
            example = "87.5", minimum = "0", maximum = "100")
    private Double performanceScore;
}