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
public interface InventoryRepository extends JpaRepository<Inventory, String> {

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
    @Query("SELECT i FROM Inventory i WHERE i.expiryDate IS NOT NULL AND i.expiryDate <= :expiryDate")
    List<Inventory> findItemsExpiringSoon(@Param("expiryDate") LocalDateTime expiryDate);

    // Find by location code
    List<Inventory> findByLocationCode(String locationCode);

    // Find by batch number
    List<Inventory> findByBatchNumber(String batchNumber);

    // Search inventory by product name or SKU
    @Query("SELECT i FROM Inventory i JOIN i.product p WHERE " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Inventory> searchInventory(@Param("keyword") String keyword, Pageable pageable);

    // Get total inventory value
    @Query("SELECT COALESCE(SUM(i.quantityOnHand * i.unitCost), 0) FROM Inventory i WHERE i.unitCost IS NOT NULL")
    BigDecimal getTotalInventoryValue();

//    // Get inventory value by warehouse
//    @Query("SELECT COALESCE(SUM(i.quantityOnHand * i.unitCost), 0) FROM Inventory i WHERE i.warehouseId = :warehouseId AND i.unitCost IS NOT NULL")
//    BigDecimal getInventoryValueByWarehouse(@Param("warehouseId") String warehouseId);

    // Get top inventory items by value
    @Query("SELECT i FROM Inventory i WHERE i.unitCost IS NOT NULL ORDER BY (i.quantityOnHand * i.unitCost) DESC")
    List<Inventory> findTopInventoryItemsByValue();

    // Count low stock items
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantityOnHand <= i.reorderLevel AND i.reorderLevel IS NOT NULL")
    Long countLowStockItems();

    // Count overstock items
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.quantityOnHand > i.maxStockLevel AND i.maxStockLevel IS NOT NULL")
    Long countOverstockItems();

    // Count out of stock items
    Long countByQuantityOnHand(Integer quantity);

    // Count items expiring soon
    @Query("SELECT COUNT(i) FROM Inventory i WHERE i.expiryDate IS NOT NULL AND i.expiryDate <= :expiryDate")
    Long countItemsExpiringSoon(@Param("expiryDate") LocalDateTime expiryDate);

    // Find items that need counting (haven't been counted recently)
    @Query("SELECT i FROM Inventory i WHERE i.lastCountedDate IS NULL OR i.lastCountedDate < :cutoffDate")
    List<Inventory> findItemsNeedingCount(@Param("cutoffDate") LocalDateTime cutoffDate);

    // Get inventory by product and warehouse with batch
    List<Inventory> findByProductIdAndWarehouseIdAndBatchNumber(String productId, String warehouseId, String batchNumber);

    // Check if inventory exists for product and warehouse
    boolean existsByProductIdAndWarehouseId(String productId, String warehouseId);
}
