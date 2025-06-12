package com.capstone.warehousesvc.services.impl;

import com.capstone.warehousesvc.dtos.InventoryAdjustmentRequest;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.TransactionRequest;
import com.capstone.warehousesvc.models.InventoryHistory;
import com.capstone.warehousesvc.repositories.InventoryHistoryRepository;
import com.capstone.warehousesvc.repositories.InventoryRepository;
import com.capstone.warehousesvc.services.InventoryService;
import com.capstone.warehousesvc.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration service that demonstrates how the inventory system works with existing warehouse functions.
 * This service shows examples of how inventory and transactions can work together.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryTransactionIntegrationService {

    private final InventoryService inventoryService;
    private final TransactionService transactionService;
    private final InventoryRepository inventoryRepository;
    private final InventoryHistoryRepository inventoryHistoryRepository;

    /**
     * Process a purchase transaction and update inventory accordingly
     */
    @Transactional
    public Response processPurchaseWithInventoryUpdate(TransactionRequest transactionRequest) {
        log.info("Processing purchase transaction with inventory update for product: {}", 
                transactionRequest.getProductId());

        try {
            // First, process the purchase transaction
            Response transactionResponse = transactionService.purchase(transactionRequest);
            
            if (transactionResponse.getStatus() != 200) {
                log.error("Transaction failed, not updating inventory");
                return transactionResponse;
            }

            // Check if inventory exists for this product and warehouse combination
            boolean inventoryExists = inventoryRepository.existsByProductIdAndWarehouseId(
                    transactionRequest.getProductId(), 
                    "default-warehouse-id" // You would get this from the transaction or context
            );

            if (inventoryExists) {
                // Update existing inventory
                InventoryAdjustmentRequest adjustmentRequest = InventoryAdjustmentRequest.builder()
                        .inventoryId(getInventoryId(transactionRequest.getProductId()))
                        .adjustmentType("ADD")
                        .quantity(transactionRequest.getQuantity())
                        .reason("Purchase Transaction")
                        .referenceNumber("TXN-" + System.currentTimeMillis())
                        .notes("Automatic inventory update from purchase transaction")
                        .build();

                Response inventoryResponse = inventoryService.adjustInventory(adjustmentRequest);
                
                if (inventoryResponse.getStatus() != 200) {
                    log.error("Inventory update failed, but transaction was successful");
                    // In a real system, you might want to compensate or alert administrators
                }
            } else {
                log.info("No inventory record found for product, transaction processed without inventory update");
            }

            return Response.builder()
                    .status(200)
                    .message("Purchase transaction and inventory update completed successfully")
                    .build();

        } catch (Exception e) {
            log.error("Error processing purchase with inventory update", e);
            return Response.builder()
                    .status(500)
                    .message("Error processing purchase: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Process a sale transaction and update inventory accordingly
     */
    @Transactional
    public Response processSaleWithInventoryUpdate(TransactionRequest transactionRequest) {
        log.info("Processing sale transaction with inventory update for product: {}", 
                transactionRequest.getProductId());

        try {
            // First, check if sufficient inventory is available
            String inventoryId = getInventoryId(transactionRequest.getProductId());
            if (inventoryId == null) {
                return Response.builder()
                        .status(400)
                        .message("No inventory found for this product")
                        .build();
            }

            // Check available quantity (this is a simplified check)
            // In a real implementation, you'd get the actual inventory record and check available quantity
            
            // Process the sale transaction
            Response transactionResponse = transactionService.sell(transactionRequest);
            
            if (transactionResponse.getStatus() != 200) {
                log.error("Transaction failed, not updating inventory");
                return transactionResponse;
            }

            // Update inventory
            InventoryAdjustmentRequest adjustmentRequest = InventoryAdjustmentRequest.builder()
                    .inventoryId(inventoryId)
                    .adjustmentType("SUBTRACT")
                    .quantity(transactionRequest.getQuantity())
                    .reason("Sale Transaction")
                    .referenceNumber("TXN-" + System.currentTimeMillis())
                    .notes("Automatic inventory reduction from sale transaction")
                    .build();

            Response inventoryResponse = inventoryService.adjustInventory(adjustmentRequest);
            
            if (inventoryResponse.getStatus() != 200) {
                log.error("Inventory update failed, but transaction was successful");
                // In a real system, you might want to compensate or alert administrators
            }

            return Response.builder()
                    .status(200)
                    .message("Sale transaction and inventory update completed successfully")
                    .build();

        } catch (Exception e) {
            log.error("Error processing sale with inventory update", e);
            return Response.builder()
                    .status(500)
                    .message("Error processing sale: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Get inventory ID for a product (simplified implementation)
     * In a real system, this would include warehouse context
     */
    private String getInventoryId(String productId) {
        // This is a simplified implementation
        // In reality, you'd need to specify which warehouse and get the inventory ID
        return inventoryRepository.findByProductId(productId)
                .stream()
                .findFirst()
                .map(inventory -> inventory.getId())
                .orElse(null);
    }

    /**
     * Create an inventory history record for manual tracking
     */
    public void createInventoryHistoryRecord(String inventoryId, 
                                           InventoryHistory.InventoryTransactionType transactionType,
                                           Integer quantityChange,
                                           Integer quantityBefore,
                                           Integer quantityAfter,
                                           String reason,
                                           String performedBy) {
        
        InventoryHistory historyRecord = InventoryHistory.builder()
                .inventory(inventoryRepository.findById(inventoryId).orElse(null))
                .transactionType(transactionType)
                .quantityChange(quantityChange)
                .quantityBefore(quantityBefore)
                .quantityAfter(quantityAfter)
                .reason(reason)
                .performedBy(performedBy)
                .performedByName("System") // You'd get this from user context
                .build();

        inventoryHistoryRepository.save(historyRecord);
        log.info("Created inventory history record for inventory: {}", inventoryId);
    }

    /**
     * Example of how to check inventory before processing orders
     */
    public boolean checkInventoryAvailability(String productId, Integer requiredQuantity) {
        return inventoryRepository.findByProductId(productId)
                .stream()
                .mapToInt(inventory -> inventory.getAvailableQuantity())
                .sum() >= requiredQuantity;
    }

    /**
     * Example of how to get total inventory value across all warehouses
     */
    public Response getTotalInventoryValue() {
        try {
            var totalValue = inventoryRepository.getTotalInventoryValue();
            
            return Response.builder()
                    .status(200)
                    .message("Success")
                    .data(java.util.Map.of("totalInventoryValue", totalValue))
                    .build();
                    
        } catch (Exception e) {
            log.error("Error calculating total inventory value", e);
            return Response.builder()
                    .status(500)
                    .message("Error calculating inventory value: " + e.getMessage())
                    .build();
        }
    }
}
