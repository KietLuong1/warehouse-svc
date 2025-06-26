package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.dashboard.*;

import java.time.LocalDateTime;
import java.util.List;

public interface DashboardService {

    DashboardDTO getDashboardSummary();

    DashboardDTO getDashboardSummaryByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    InventoryMetrics getInventoryMetrics();

    TransactionMetrics getTransactionMetrics();

    TransactionMetrics getTransactionMetricsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    ProductionMetrics getProductionMetrics();

    ProductionMetrics getProductionMetricsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<RecentActivity> getRecentActivities(int limit);

    List<TopProduct> getTopProductsByValue(int limit);

    List<LowStockAlert> getLowStockAlerts();

    List<TransactionTrend> getTransactionTrends(String period); // DAILY, WEEKLY, MONTHLY
}