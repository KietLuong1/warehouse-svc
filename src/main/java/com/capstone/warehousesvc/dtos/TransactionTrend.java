
package com.capstone.warehousesvc.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transaction trend data for a specific period")
public class TransactionTrend {

    @Schema(description = "Time period for the trend data",
            example = "DAILY", allowableValues = {"DAILY", "WEEKLY", "MONTHLY"})
    private String period;

    @Schema(description = "Total transaction amount for the period",
            example = "5000.00", minimum = "0")
    private BigDecimal amount;

    @Schema(description = "Number of transactions in the period",
            example = "25", minimum = "0")
    private Integer count;

    @Schema(description = "Date/time for the trend data point",
            example = "2024-01-15T00:00:00")
    private LocalDateTime date;
}