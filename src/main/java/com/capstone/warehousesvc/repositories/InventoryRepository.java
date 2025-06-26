package com.capstone.warehousesvc.repositories;

import com.capstone.warehousesvc.models.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, String>, JpaSpecificationExecutor<Inventory> {

    // Find inventory by product and warehouse
    Optional<Inventory> findByProductIdAndWarehouseId(String productId, String warehouseId);

    // Find all inventory for a specific product
    List<Inventory> findByProductId(String productId);

    // Find all inventory for a specific warehouse
    List<Inventory> findByWarehouseId(String warehouseId);

    // Find low stock items
    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand <= i.reorderLevel AND i.reorderLevel IS NOT NULL")
    List<Inventory> findLowStockItems();

    // Find overstock items
    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand > i.maxStockLevel AND i.maxStockLevel IS NOT NULL")
    List<Inventory> findOverstockItems();

    // Find out of stock items
    List<Inventory> findByQuantityOnHand(Integer quantity);

    // Find items expiring soon
    @Query("SELECT i FROM Inventory i WHERE i.expiryDate <= :expiryDate AND i.expiryDate IS NOT NULL")
    List<Inventory> findItemsExpiringSoon(@Param("expiryDate") LocalDateTime expiryDate);

    // Find by location code
    List<Inventory> findByLocationCode(String locationCode);

    // Find by batch number
    List<Inventory> findByBatchNumber(String batchNumber);

    // Analytics queries
    @Query("SELECT SUM(i.quantityOnHand * i.unitCost) FROM Inventory i WHERE i.unitCost IS NOT NULL")
    BigDecimal getTotalInventoryValue();

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantityOnHand <= i.reorderLevel AND i.reorderLevel IS NOT NULL")
    Long countLowStockItems();

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantityOnHand > i.maxStockLevel AND i.maxStockLevel IS NOT NULL")
    Long countOverstockItems();

    Long countByQuantityOnHand(Integer quantity);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.expiryDate <= :expiryDate AND i.expiryDate IS NOT NULL")
    Long countItemsExpiringSoon(@Param("expiryDate") LocalDateTime expiryDate);

    // Check if product-warehouse combination exists
    Boolean existsByProductIdAndWarehouseId(String productId, String warehouseId);

    // Search functionality
    @Query("SELECT i FROM Inventory i WHERE " +
           "(LOWER(i.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.product.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY i.lastUpdated DESC")
    Page<Inventory> searchInventory(@Param("keyword") String keyword, Pageable pageable);

    // Soft delete methods
    @Query("SELECT i FROM Inventory i WHERE i.isDeleted = false OR i.isDeleted IS NULL")
    List<Inventory> findAllActive();

    @Query("SELECT i FROM Inventory i WHERE i.isDeleted = false OR i.isDeleted IS NULL")
    Page<Inventory> findAllActive(Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE i.id = :id AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    Optional<Inventory> findActiveById(@Param("id") String id);

    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND i.warehouse.id = :warehouseId AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    Optional<Inventory> findActiveByProductIdAndWarehouseId(@Param("productId") String productId, @Param("warehouseId") String warehouseId);

    @Query("SELECT i FROM Inventory i WHERE i.product.id = :productId AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    List<Inventory> findActiveByProductId(@Param("productId") String productId);

    @Query("SELECT i FROM Inventory i WHERE i.warehouse.id = :warehouseId AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    List<Inventory> findActiveByWarehouseId(@Param("warehouseId") String warehouseId);

    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand <= i.reorderLevel AND i.reorderLevel IS NOT NULL AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    List<Inventory> findActiveLowStockItems();

    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand > i.maxStockLevel AND i.maxStockLevel IS NOT NULL AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    List<Inventory> findActiveOverstockItems();

    @Query("SELECT i FROM Inventory i WHERE i.quantityOnHand = :quantity AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    List<Inventory> findActiveByQuantityOnHand(@Param("quantity") Integer quantity);

    @Query("SELECT i FROM Inventory i WHERE i.expiryDate <= :expiryDate AND i.expiryDate IS NOT NULL AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    List<Inventory> findActiveItemsExpiringSoon(@Param("expiryDate") LocalDateTime expiryDate);

    @Query("SELECT i FROM Inventory i WHERE i.locationCode = :locationCode AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    List<Inventory> findActiveByLocationCode(@Param("locationCode") String locationCode);

    @Query("SELECT i FROM Inventory i WHERE i.batchNumber = :batchNumber AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    List<Inventory> findActiveByBatchNumber(@Param("batchNumber") String batchNumber);

    @Query("SELECT i FROM Inventory i WHERE " +
           "(LOWER(i.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.product.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "AND (i.isDeleted = false OR i.isDeleted IS NULL) " +
           "ORDER BY i.lastUpdated DESC")
    Page<Inventory> searchActiveInventory(@Param("keyword") String keyword, Pageable pageable);

    // Soft delete analytics
    @Query("SELECT SUM(i.quantityOnHand * i.unitCost) FROM Inventory i WHERE i.unitCost IS NOT NULL AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    BigDecimal getActiveTotalInventoryValue();

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantityOnHand <= i.reorderLevel AND i.reorderLevel IS NOT NULL AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    Long countActiveLowStockItems();

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantityOnHand > i.maxStockLevel AND i.maxStockLevel IS NOT NULL AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    Long countActiveOverstockItems();

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantityOnHand = :quantity AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    Long countActiveByQuantityOnHand(@Param("quantity") Integer quantity);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.expiryDate <= :expiryDate AND i.expiryDate IS NOT NULL AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    Long countActiveItemsExpiringSoon(@Param("expiryDate") LocalDateTime expiryDate);

    @Query("SELECT COUNT(i) FROM Inventory i WHERE (i.isDeleted = false OR i.isDeleted IS NULL)")
    Long countActive();

    // Additional methods for business logic
    @Query("SELECT i FROM Inventory i WHERE (i.isDeleted = false OR i.isDeleted IS NULL) ORDER BY (i.quantityOnHand * i.unitCost) DESC")
    List<Inventory> findTopInventoryItemsByValue(Pageable pageable);

    @Query("SELECT i FROM Inventory i WHERE (i.lastCountedDate IS NULL OR i.lastCountedDate <= :cutoffDate) AND (i.isDeleted = false OR i.isDeleted IS NULL)")
    List<Inventory> findItemsNeedingCount(@Param("cutoffDate") LocalDateTime cutoffDate);
}
