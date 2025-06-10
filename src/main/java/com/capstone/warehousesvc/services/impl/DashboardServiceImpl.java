package com.capstone.warehousesvc.services.impl;

import com.capstone.warehousesvc.dtos.*;
import com.capstone.warehousesvc.enums.TransactionStatus;
import com.capstone.warehousesvc.enums.TransactionType;
import com.capstone.warehousesvc.models.Product;
import com.capstone.warehousesvc.models.Transaction;
import com.capstone.warehousesvc.repositories.ProductRepository;
import com.capstone.warehousesvc.repositories.TransactionRepository;
import com.capstone.warehousesvc.repositories.CategoryRepository;
import com.capstone.warehousesvc.repositories.SupplierRepository;
import com.capstone.warehousesvc.services.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DashboardServiceImpl implements DashboardService {

    private final ProductRepository productRepository;
    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    private static final int LOW_STOCK_THRESHOLD = 10;
    private static final int EXPIRY_WARNING_DAYS = 30;

    @Override
    public DashboardDTO getDashboardSummary() {
        return getDashboardSummaryByDateRange(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
        );
    }

    @Override
    public DashboardDTO getDashboardSummaryByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Generating dashboard summary for period: {} to {}", startDate, endDate);

        return DashboardDTO.builder()
                .inventoryMetrics(getInventoryMetrics())
                .transactionMetrics(getTransactionMetricsByDateRange(startDate, endDate))
                .productionMetrics(getProductionMetricsByDateRange(startDate, endDate))
                .recentActivities(getRecentActivities(10))
                .trends(generateTrends(startDate, endDate))
                .build();
    }

    @Override
    public InventoryMetrics getInventoryMetrics() {
        List<Product> allProducts = productRepository.findAll();

        int totalProducts = allProducts.size();
        int lowStockProducts = (int) allProducts.stream()
                .filter(p -> p.getStockQuantity() != null && p.getStockQuantity() < LOW_STOCK_THRESHOLD)
                .count();
        int outOfStockProducts = (int) allProducts.stream()
                .filter(p -> p.getStockQuantity() == null || p.getStockQuantity() == 0)
                .count();

        BigDecimal totalInventoryValue = allProducts.stream()
                .filter(p -> p.getPrice() != null && p.getStockQuantity() != null)
                .map(p -> p.getPrice().multiply(new BigDecimal(p.getStockQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int expiringSoon = (int) allProducts.stream()
                .filter(p -> p.getExpiryDate() != null &&
                        p.getExpiryDate().isBefore(LocalDateTime.now().plusDays(EXPIRY_WARNING_DAYS)))
                .count();

        double averageStockLevel = allProducts.stream()
                .filter(p -> p.getStockQuantity() != null)
                .mapToInt(Product::getStockQuantity)
                .average()
                .orElse(0.0);

        return InventoryMetrics.builder()
                .totalProducts(totalProducts)
                .lowStockProducts(lowStockProducts)
                .outOfStockProducts(outOfStockProducts)
                .totalInventoryValue(totalInventoryValue)
                .expiringSoon(expiringSoon)
                .averageStockLevel(averageStockLevel)
                .topProductsByValue(getTopProductsByValue(5))
                .lowStockAlerts(getLowStockAlerts())
                .build();
    }

    @Override
    public TransactionMetrics getTransactionMetrics() {
        return getTransactionMetricsByDateRange(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
        );
    }

    @Override
    public TransactionMetrics getTransactionMetricsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository.findByCreatedAtBetween(startDate, endDate);

        BigDecimal totalSales = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.SALE)
                .map(Transaction::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPurchases = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.PURCHASE)
                .map(Transaction::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalReturns = transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.RETURN_TO_SUPPLIER)
                .map(Transaction::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int completedTransactions = (int) transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.COMPLETED)
                .count();

        int pendingTransactions = (int) transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.PENDING)
                .count();

        int processingTransactions = (int) transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.PROCESSING)
                .count();

        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        BigDecimal dailyRevenue = transactions.stream()
                .filter(t -> t.getCreatedAt().isAfter(today))
                .filter(t -> t.getTransactionType() == TransactionType.SALE)
                .map(Transaction::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
        BigDecimal monthlyRevenue = transactions.stream()
                .filter(t -> t.getCreatedAt().isAfter(monthStart))
                .filter(t -> t.getTransactionType() == TransactionType.SALE)
                .map(Transaction::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return TransactionMetrics.builder()
                .totalSales(totalSales)
                .totalPurchases(totalPurchases)
                .totalReturns(totalReturns)
                .completedTransactions(completedTransactions)
                .pendingTransactions(pendingTransactions)
                .processingTransactions(processingTransactions)
                .dailyRevenue(dailyRevenue)
                .monthlyRevenue(monthlyRevenue)
                .trends(getTransactionTrends("DAILY"))
                .build();
    }

    @Override
    public ProductionMetrics getProductionMetrics() {
        return getProductionMetricsByDateRange(
                LocalDateTime.now().minusDays(30),
                LocalDateTime.now()
        );
    }

    @Override
    public ProductionMetrics getProductionMetricsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> productionTransactions = transactionRepository
                .findByCreatedAtBetweenAndTransactionType(startDate, endDate, TransactionType.PURCHASE);

        int totalProductsProduced = productionTransactions.stream()
                .mapToInt(t -> t.getTotalProducts() != null ? t.getTotalProducts() : 0)
                .sum();

        int activeSuppliers = (int) supplierRepository.count();

        BigDecimal productionValue = productionTransactions.stream()
                .map(Transaction::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        double productionEfficiency = calculateProductionEfficiency(productionTransactions);

        List<CategoryPerformance> categoryPerformance = getCategoryPerformance();

        Map<String, Integer> monthlyProduction = getMonthlyProductionData(startDate, endDate);

        return ProductionMetrics.builder()
                .totalProductsProduced(totalProductsProduced)
                .activeSuppliers(activeSuppliers)
                .productionValue(productionValue)
                .productionEfficiency(productionEfficiency)
                .categoryPerformance(categoryPerformance)
                .monthlyProduction(monthlyProduction)
                .build();
    }

    @Override
    public List<RecentActivity> getRecentActivities(int limit) {
        List<Transaction> recentTransactions = transactionRepository
                .findTop10ByOrderByCreatedAtDesc();

        return recentTransactions.stream()
                .limit(limit)
                .map(this::mapToRecentActivity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TopProduct> getTopProductsByValue(int limit) {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .filter(p -> p.getPrice() != null && p.getStockQuantity() != null)
                .sorted((p1, p2) -> {
                    BigDecimal value1 = p1.getPrice().multiply(new BigDecimal(p1.getStockQuantity()));
                    BigDecimal value2 = p2.getPrice().multiply(new BigDecimal(p2.getStockQuantity()));
                    return value2.compareTo(value1);
                })
                .limit(limit)
                .map(this::mapToTopProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<LowStockAlert> getLowStockAlerts() {
        List<Product> lowStockProducts = productRepository.findByStockQuantityLessThan(LOW_STOCK_THRESHOLD);

        return lowStockProducts.stream()
                .map(this::mapToLowStockAlert)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionTrend> getTransactionTrends(String period) {
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = switch (period.toLowerCase()) {
            case "daily" -> endDate.minusDays(7);
            case "weekly" -> endDate.minusDays(28);
            case "monthly" -> endDate.minusDays(365);
            default -> endDate.minusDays(30);
        };

        List<Transaction> transactions = transactionRepository.findByCreatedAtBetween(startDate, endDate);

        return transactions.stream()
                .filter(t -> t.getTransactionType() == TransactionType.SALE)
                .collect(Collectors.groupingBy(
                        t -> t.getCreatedAt().truncatedTo(ChronoUnit.DAYS),
                        Collectors.reducing(
                                new TransactionTrend(period, BigDecimal.ZERO, 0, null),
                                t -> new TransactionTrend(period, t.getTotalPrice(), 1, t.getCreatedAt()),
                                (t1, t2) -> new TransactionTrend(
                                        period,
                                        t1.getAmount().add(t2.getAmount()),
                                        t1.getCount() + t2.getCount(),
                                        t1.getDate()
                                )
                        )
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(TransactionTrend::getDate))
                .collect(Collectors.toList());
    }

    // Helper methods

    private RecentActivity mapToRecentActivity(Transaction transaction) {
        return RecentActivity.builder()
                .id(transaction.getId())
                .activityType(transaction.getTransactionType().toString())
                .description(transaction.getDescription())
                .userId(transaction.getUserId())
                .username(transaction.getUsername())
                .timestamp(transaction.getCreatedAt())
                .status(transaction.getStatus().toString())
                .build();
    }

    private TopProduct mapToTopProduct(Product product) {
        BigDecimal value = product.getPrice() != null && product.getStockQuantity() != null
                ? product.getPrice().multiply(new BigDecimal(product.getStockQuantity()))
                : BigDecimal.ZERO;

        return TopProduct.builder()
                .productId(product.getId())
                .productName(product.getName())
                .sku(product.getSku())
                .value(value)
                .quantity(product.getStockQuantity())
                .build();
    }

    private LowStockAlert mapToLowStockAlert(Product product) {
        String severity = determineSeverity(product.getStockQuantity());

        return LowStockAlert.builder()
                .productId(product.getId())
                .productName(product.getName())
                .sku(product.getSku())
                .currentStock(product.getStockQuantity())
                .minimumThreshold(LOW_STOCK_THRESHOLD)
                .severity(severity)
                .build();
    }

    private String determineSeverity(Integer stockQuantity) {
        if (stockQuantity == null || stockQuantity == 0) return "CRITICAL";
        if (stockQuantity <= 2) return "HIGH";
        if (stockQuantity <= 5) return "MEDIUM";
        return "LOW";
    }

    private double calculateProductionEfficiency(List<Transaction> transactions) {
        if (transactions.isEmpty()) return 0.0;

        long completedCount = transactions.stream()
                .filter(t -> t.getStatus() == TransactionStatus.COMPLETED)
                .count();

        return ((double) completedCount / transactions.size()) * 100.0;
    }

    private List<CategoryPerformance> getCategoryPerformance() {
        // Implementation would require category repository methods
        // This is a placeholder implementation
        return new ArrayList<>();
    }

    private Map<String, Integer> getMonthlyProductionData(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = transactionRepository
                .findByCreatedAtBetweenAndTransactionType(startDate, endDate, TransactionType.PURCHASE);

        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getCreatedAt().getMonth().toString(),
                        Collectors.summingInt(t -> t.getTotalProducts() != null ? t.getTotalProducts() : 0)
                ));
    }

    private Map<String, Object> generateTrends(LocalDateTime startDate, LocalDateTime endDate) {
        Map<String, Object> trends = new HashMap<>();

        // Calculate various trend metrics
        trends.put("salesGrowth", calculateSalesGrowth(startDate, endDate));
        trends.put("inventoryTurnover", calculateInventoryTurnover());
        trends.put("averageOrderValue", calculateAverageOrderValue(startDate, endDate));

        return trends;
    }

    private Double calculateSalesGrowth(LocalDateTime startDate, LocalDateTime endDate) {
        // Implementation for sales growth calculation
        return 0.0; // Placeholder
    }

    private Double calculateInventoryTurnover() {
        // Implementation for inventory turnover calculation
        return 0.0; // Placeholder
    }

    private BigDecimal calculateAverageOrderValue(LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> salesTransactions = transactionRepository
                .findByCreatedAtBetweenAndTransactionType(startDate, endDate, TransactionType.SALE);

        if (salesTransactions.isEmpty()) return BigDecimal.ZERO;

        BigDecimal totalSales = salesTransactions.stream()
                .map(Transaction::getTotalPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalSales.divide(new BigDecimal(salesTransactions.size()), 2, RoundingMode.HALF_UP);
    }
}