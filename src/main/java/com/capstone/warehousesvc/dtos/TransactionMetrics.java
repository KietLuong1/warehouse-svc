package com.capstone.warehousesvc.dtos;

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
@Schema(description = "Transaction analytics and financial metrics")
public class TransactionMetrics {

    @Schema(description = "Total value of all sales transactions",
            example = "50000.00", minimum = "0")
    private BigDecimal totalSales;

    @Schema(description = "Total value of all purchase transactions",
            example = "30000.00", minimum = "0")
    private BigDecimal totalPurchases;

    @Schema(description = "Total value of all return transactions",
            example = "2500.00", minimum = "0")
    private BigDecimal totalReturns;

    @Schema(description = "Number of completed transactions",
            example = "156", minimum = "0")
    private Integer completedTransactions;

    @Schema(description = "Number of pending transactions",
            example = "12", minimum = "0")
    private Integer pendingTransactions;

    @Schema(description = "Number of processing transactions",
            example = "8", minimum = "0")
    private Integer processingTransactions;

    @Schema(description = "Revenue generated today",
            example = "1500.00", minimum = "0")
    private BigDecimal dailyRevenue;

    @Schema(description = "Revenue generated this month",
            example = "25000.00", minimum = "0")
    private BigDecimal monthlyRevenue;

    @Schema(description = "Transaction trend data over time periods")
    private List<TransactionTrend> trends;
}