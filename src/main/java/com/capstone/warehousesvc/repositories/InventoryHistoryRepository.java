package com.capstone.warehousesvc.repositories;

import com.capstone.warehousesvc.models.InventoryHistory;
import com.capstone.warehousesvc.models.InventoryHistory.InventoryTransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, String> {

    // Find history by inventory ID
    List<InventoryHistory> findByInventoryIdOrderByCreatedAtDesc(String inventoryId);

    // Find history by transaction type
    List<InventoryHistory> findByTransactionTypeOrderByCreatedAtDesc(InventoryTransactionType transactionType);

    // Find history by date range
    @Query("SELECT ih FROM InventoryHistory ih WHERE ih.createdAt BETWEEN :startDate AND :endDate ORDER BY ih.createdAt DESC")
    List<InventoryHistory> findByDateRangeOrderByCreatedAtDesc(
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);

    // Find history by user
    List<InventoryHistory> findByPerformedByOrderByCreatedAtDesc(String performedBy);

    // Find history by inventory and date range
    @Query("SELECT ih FROM InventoryHistory ih WHERE ih.inventory.id = :inventoryId AND ih.createdAt BETWEEN :startDate AND :endDate ORDER BY ih.createdAt DESC")
    List<InventoryHistory> findByInventoryAndDateRangeOrderByCreatedAtDesc(
            @Param("inventoryId") String inventoryId,
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);

    // Find recent history for all inventory (useful for audit reports)
    @Query("SELECT ih FROM InventoryHistory ih WHERE ih.createdAt >= :cutoffDate ORDER BY ih.createdAt DESC")
    List<InventoryHistory> findRecentHistoryOrderByCreatedAtDesc(@Param("cutoffDate") LocalDateTime cutoffDate);

    // Find history by reference number
    List<InventoryHistory> findByReferenceNumberOrderByCreatedAtDesc(String referenceNumber);

    // Get count of transactions by type in date range
    @Query("SELECT COUNT(ih) FROM InventoryHistory ih WHERE ih.transactionType = :transactionType AND ih.createdAt BETWEEN :startDate AND :endDate")
    Long countByTransactionTypeAndDateRange(
            @Param("transactionType") InventoryTransactionType transactionType,
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate);

    // Find history by product (through inventory relationship)
    @Query("SELECT ih FROM InventoryHistory ih WHERE ih.inventory.product.id = :productId ORDER BY ih.createdAt DESC")
    List<InventoryHistory> findByProductIdOrderByCreatedAtDesc(@Param("productId") String productId);

    // Find history by warehouse (through inventory relationship)
    @Query("SELECT ih FROM InventoryHistory ih WHERE ih.inventory.warehouse.id = :warehouseId ORDER BY ih.createdAt DESC")
    List<InventoryHistory> findByWarehouseIdOrderByCreatedAtDesc(@Param("warehouseId") String warehouseId);
}
