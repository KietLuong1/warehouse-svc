package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.*;

import java.util.List;

public interface InventoryService {

    // CRUD Operations
    Response createInventory(InventoryDTO inventoryDTO);
    
    Response updateInventory(String id, InventoryDTO inventoryDTO);
    
    Response getInventoryById(String id);
    
    Response getAllInventory(int page, int size, String keyword, String warehouseId);
    
    Response deleteInventory(String id);

    // Search and Filter Operations
    Response searchInventory(String keyword, int page, int size);
    
    Response getInventoryByProduct(String productId);
    
    Response getInventoryByWarehouse(String warehouseId);
    
    Response getInventoryByProductAndWarehouse(String productId, String warehouseId);

    // Inventory Management Operations
    Response adjustInventory(InventoryAdjustmentRequest request);
    
    Response moveInventory(InventoryMovementRequest request);
    
    Response reserveInventory(String inventoryId, Integer quantity);
    
    Response releaseReservation(String inventoryId, Integer quantity);

    // Analytics and Reporting
    Response getInventorySummary();
    
    Response getLowStockItems();
    
    Response getOverstockItems();
    
    Response getOutOfStockItems();
    
    Response getExpiringItems(Integer daysAhead);
    
    Response getTopInventoryItemsByValue(Integer limit);

    // Location and Batch Management
    Response getInventoryByLocation(String locationCode);
    
    Response getInventoryByBatch(String batchNumber);
    
    Response updateInventoryLocation(String inventoryId, String locationCode);

    // Inventory Count and Audit
    Response getItemsNeedingCount(Integer daysSinceLastCount);
    
    Response updateLastCountedDate(String inventoryId);
    
    Response performInventoryCount(String inventoryId, Integer countedQuantity, String countedBy);
}
