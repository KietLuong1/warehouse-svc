package com.capstone.warehousesvc.controllers;

import com.capstone.warehousesvc.dtos.*;
import com.capstone.warehousesvc.dtos.dashboard.*;
import com.capstone.warehousesvc.services.DashboardService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Tag(name = "Dashboard", description = "Dashboard analytics and metrics")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173", "http://localhost:4200", "http://127.0.0.1:3000"})
@RestController
@RequestMapping("/api/v1/dashboard")  // Updated path
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    @Operation(summary = "Get dashboard summary", description = "Get complete dashboard summary with all metrics")
    @GetMapping("/summary")
    public ResponseEntity<Response> getDashboardSummary() {
        log.info("Fetching dashboard summary");
        DashboardDTO summary = dashboardService.getDashboardSummary();

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Dashboard summary retrieved successfully")
                .data(summary)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get dashboard summary by date range")
    @GetMapping("/summary/date-range")
    public ResponseEntity<Response> getDashboardSummaryByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Fetching dashboard summary for date range: {} to {}", startDate, endDate);
        DashboardDTO summary = dashboardService.getDashboardSummaryByDateRange(startDate, endDate);

        Map<String, Object> data = new HashMap<>();
        data.put("dashboard", summary);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Dashboard summary for date range retrieved successfully")
                .dataList(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get inventory metrics")
    @GetMapping("/inventory")
    public ResponseEntity<Response> getInventoryMetrics() {
        log.info("Fetching inventory metrics");
        InventoryMetrics metrics = dashboardService.getInventoryMetrics();

        Map<String, Object> data = new HashMap<>();
        data.put("inventory", metrics);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Inventory metrics retrieved successfully")
                .dataList(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction metrics")
    @GetMapping("/transactions")
    public ResponseEntity<Response> getTransactionMetrics() {
        log.info("Fetching transaction metrics");
        TransactionMetrics metrics = dashboardService.getTransactionMetrics();

        Map<String, Object> data = new HashMap<>();
        data.put("transactions", metrics);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Transaction metrics retrieved successfully")
                .dataList(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction metrics by date range")
    @GetMapping("/transactions/date-range")
    public ResponseEntity<Response> getTransactionMetricsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Fetching transaction metrics for date range: {} to {}", startDate, endDate);
        TransactionMetrics metrics = dashboardService.getTransactionMetricsByDateRange(startDate, endDate);

        Map<String, Object> data = new HashMap<>();
        data.put("transactions", metrics);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Transaction metrics for date range retrieved successfully")
                .dataList(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get production metrics")
    @GetMapping("/production")
    public ResponseEntity<Response> getProductionMetrics() {
        log.info("Fetching production metrics");
        ProductionMetrics metrics = dashboardService.getProductionMetrics();

        Map<String, Object> data = new HashMap<>();
        data.put("production", metrics);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Production metrics retrieved successfully")
                .dataList(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get production metrics by date range")
    @GetMapping("/production/date-range")
    public ResponseEntity<Response> getProductionMetricsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("Fetching production metrics for date range: {} to {}", startDate, endDate);
        ProductionMetrics metrics = dashboardService.getProductionMetricsByDateRange(startDate, endDate);

        Map<String, Object> data = new HashMap<>();
        data.put("production", metrics);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Production metrics for date range retrieved successfully")
                .dataList(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get recent activities")
    @GetMapping("/recent-activities")
    public ResponseEntity<Response> getRecentActivities(
            @Parameter(description = "Limit number of activities") @RequestParam(defaultValue = "10") int limit) {

        log.info("Fetching recent activities with limit: {}", limit);
        List<RecentActivity> activities = dashboardService.getRecentActivities(limit);

        Map<String, Object> data = new HashMap<>();
        data.put("activities", activities);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Recent activities retrieved successfully")
                .dataList(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get top products by value")
    @GetMapping("/top-products")
    public ResponseEntity<Response> getTopProductsByValue(
            @Parameter(description = "Limit number of products") @RequestParam(defaultValue = "5") int limit) {

        log.info("Fetching top products by value with limit: {}", limit);
        List<TopProduct> topProducts = dashboardService.getTopProductsByValue(limit);

        Map<String, Object> data = new HashMap<>();
        data.put("products", topProducts);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Top products retrieved successfully")
                .dataList(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get low stock alerts")
    @GetMapping("/low-stock-alerts")
    public ResponseEntity<Response> getLowStockAlerts() {
        log.info("Fetching low stock alerts");
        List<LowStockAlert> alerts = dashboardService.getLowStockAlerts();

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Low stock alerts retrieved successfully")
                .data(alerts)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get transaction trends")
    @GetMapping("/transaction-trends")
    public ResponseEntity<Response> getTransactionTrends(
            @Parameter(description = "Period for trends (DAILY, WEEKLY, MONTHLY)")
            @Schema(allowableValues = {"DAILY", "WEEKLY", "MONTHLY"})
            @RequestParam(defaultValue = "DAILY") String period) {

        List<TransactionTrend> trends = dashboardService.getTransactionTrends(period);

        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Transaction trends retrieved successfully")
                .data(trends)
                .build();

        return ResponseEntity.ok(response);
    }


}