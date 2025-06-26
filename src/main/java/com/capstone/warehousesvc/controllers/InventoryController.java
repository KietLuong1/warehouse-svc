package com.capstone.warehousesvc.controllers;

import com.capstone.warehousesvc.dtos.*;
import com.capstone.warehousesvc.services.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Inventory", description = "Comprehensive inventory management operations")
@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @Operation(summary = "Create new inventory record",
            description = "Create a new inventory record for a product in a warehouse (Admin only)")
    @PostMapping("/create")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> createInventory(@Valid @RequestBody InventoryDTO inventoryDTO) {
        return ResponseEntity.ok(inventoryService.createInventory(inventoryDTO));
    }

    @Operation(summary = "Update inventory record",
            description = "Update an existing inventory record (Admin only)")
    @PutMapping("/update/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateInventory(
            @Parameter(description = "Inventory ID") @PathVariable String id,
            @Valid @RequestBody InventoryDTO inventoryDTO) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDTO));
    }

    @Operation(summary = "Get inventory by ID",
            description = "Retrieve inventory details by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getInventoryById(
            @Parameter(description = "Inventory ID") @PathVariable String id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @Operation(summary = "Get all inventory records",
            description = "Retrieve all inventory records with pagination")
    @GetMapping("/all")
    public ResponseEntity<Response> getAllInventory(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                    @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                    @RequestParam (value = "keyword", required = false) String keyword,
                                                    @RequestParam (value = "warehouseId", required = false) String warehouseId ){
        return ResponseEntity.ok(inventoryService.getAllInventory(page, size, keyword, warehouseId));
    }

    @Operation(summary = "Delete inventory record",
            description = "Delete an inventory record (Admin only)")
    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteInventory(
            @Parameter(description = "Inventory ID") @PathVariable String id) {
        return ResponseEntity.ok(inventoryService.deleteInventory(id));
    }

    @Operation(summary = "Search inventory",
            description = "Search inventory by product name or SKU")
    @GetMapping("/search")
    public ResponseEntity<Response> searchInventory(
            @Parameter(description = "Search term") @RequestParam("keyword") String keyword,
            @Parameter(description = "Page (zero-based)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Size") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(inventoryService.searchInventory(keyword, page, size));
    }

    @Operation(summary = "Get inventory by product",
            description = "Get all inventory records for a specific product")
    @GetMapping("/product/{productId}")
    public ResponseEntity<Response> getInventoryByProduct(
            @Parameter(description = "Product ID") @PathVariable String productId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProduct(productId));
    }

    @Operation(summary = "Get inventory by warehouse",
            description = "Get all inventory records for a specific warehouse")
    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<Response> getInventoryByWarehouse(
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        return ResponseEntity.ok(inventoryService.getInventoryByWarehouse(warehouseId));
    }

    @Operation(summary = "Get inventory by product and warehouse",
            description = "Get inventory record for a specific product in a specific warehouse")
    @GetMapping("/product/{productId}/warehouse/{warehouseId}")
    public ResponseEntity<Response> getInventoryByProductAndWarehouse(
            @Parameter(description = "Product ID") @PathVariable String productId,
            @Parameter(description = "Warehouse ID") @PathVariable String warehouseId) {
        return ResponseEntity.ok(inventoryService.getInventoryByProductAndWarehouse(productId, warehouseId));
    }

    @Operation(summary = "Adjust inventory quantity",
            description = "Adjust inventory quantity (add, subtract, or set) (Admin only)")
    @PostMapping("/adjust")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> adjustInventory(@Valid @RequestBody InventoryAdjustmentRequest request) {
        return ResponseEntity.ok(inventoryService.adjustInventory(request));
    }

    @Operation(summary = "Move inventory between warehouses",
            description = "Transfer inventory from one warehouse to another (Admin only)")
    @PostMapping("/move")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> moveInventory(@Valid @RequestBody InventoryMovementRequest request) {
        return ResponseEntity.ok(inventoryService.moveInventory(request));
    }

    @Operation(summary = "Reserve inventory",
            description = "Reserve a quantity of inventory for orders (Admin only)")
    @PostMapping("/{id}/reserve")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> reserveInventory(
            @Parameter(description = "Inventory ID") @PathVariable String id,
            @Parameter(description = "Quantity to reserve") @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.reserveInventory(id, quantity));
    }

    @Operation(summary = "Release inventory reservation",
            description = "Release reserved inventory quantity (Admin only)")
    @PostMapping("/{id}/release")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> releaseReservation(
            @Parameter(description = "Inventory ID") @PathVariable String id,
            @Parameter(description = "Quantity to release") @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.releaseReservation(id, quantity));
    }

    // Analytics and Reporting

    @Operation(summary = "Get inventory summary",
            description = "Get comprehensive inventory analytics and summary")
    @GetMapping("/summary")
    public ResponseEntity<Response> getInventorySummary() {
        return ResponseEntity.ok(inventoryService.getInventorySummary());
    }

    @Operation(summary = "Get low stock items",
            description = "Get items that are below their reorder level")
    @GetMapping("/alerts/low-stock")
    public ResponseEntity<Response> getLowStockItems() {
        return ResponseEntity.ok(inventoryService.getLowStockItems());
    }

    @Operation(summary = "Get overstock items",
            description = "Get items that exceed their maximum stock level")
    @GetMapping("/alerts/overstock")
    public ResponseEntity<Response> getOverstockItems() {
        return ResponseEntity.ok(inventoryService.getOverstockItems());
    }

    @Operation(summary = "Get out of stock items",
            description = "Get items with zero quantity")
    @GetMapping("/alerts/out-of-stock")
    public ResponseEntity<Response> getOutOfStockItems() {
        return ResponseEntity.ok(inventoryService.getOutOfStockItems());
    }

    @Operation(summary = "Get expiring items",
            description = "Get items that will expire within specified days")
    @GetMapping("/alerts/expiring")
    public ResponseEntity<Response> getExpiringItems(
            @Parameter(description = "Days ahead to check for expiry", example = "30")
            @RequestParam(defaultValue = "30") Integer daysAhead) {
        return ResponseEntity.ok(inventoryService.getExpiringItems(daysAhead));
    }

    @Operation(summary = "Get top inventory items by value",
            description = "Get top inventory items sorted by total value")
    @GetMapping("/top-value")
    public ResponseEntity<Response> getTopInventoryItemsByValue(
            @Parameter(description = "Number of items to return", example = "10")
            @RequestParam(defaultValue = "10") Integer limit) {
        return ResponseEntity.ok(inventoryService.getTopInventoryItemsByValue(limit));
    }

    // Location and Batch Management

    @Operation(summary = "Get inventory by location",
            description = "Get inventory records for a specific location code")
    @GetMapping("/location/{locationCode}")
    public ResponseEntity<Response> getInventoryByLocation(
            @Parameter(description = "Location code") @PathVariable String locationCode) {
        return ResponseEntity.ok(inventoryService.getInventoryByLocation(locationCode));
    }

    @Operation(summary = "Get inventory by batch",
            description = "Get inventory records for a specific batch number")
    @GetMapping("/batch/{batchNumber}")
    public ResponseEntity<Response> getInventoryByBatch(
            @Parameter(description = "Batch number") @PathVariable String batchNumber) {
        return ResponseEntity.ok(inventoryService.getInventoryByBatch(batchNumber));
    }

    @Operation(summary = "Update inventory location",
            description = "Update the location code for an inventory record (Admin only)")
    @PutMapping("/{id}/location")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateInventoryLocation(
            @Parameter(description = "Inventory ID") @PathVariable String id,
            @Parameter(description = "New location code") @RequestParam String locationCode) {
        return ResponseEntity.ok(inventoryService.updateInventoryLocation(id, locationCode));
    }

    // Inventory Count and Audit

    @Operation(summary = "Get items needing physical count",
            description = "Get items that haven't been physically counted recently")
    @GetMapping("/count/needed")
    public ResponseEntity<Response> getItemsNeedingCount(
            @Parameter(description = "Days since last count", example = "90")
            @RequestParam(defaultValue = "90") Integer daysSinceLastCount) {
        return ResponseEntity.ok(inventoryService.getItemsNeedingCount(daysSinceLastCount));
    }

    @Operation(summary = "Update last counted date",
            description = "Mark an inventory item as counted today (Admin only)")
    @PostMapping("/{id}/count/mark")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateLastCountedDate(
            @Parameter(description = "Inventory ID") @PathVariable String id) {
        return ResponseEntity.ok(inventoryService.updateLastCountedDate(id));
    }

    @Operation(summary = "Perform inventory count",
            description = "Record a physical inventory count and adjust quantities (Admin only)")
    @PostMapping("/{id}/count")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> performInventoryCount(
            @Parameter(description = "Inventory ID") @PathVariable String id,
            @Parameter(description = "Physically counted quantity") @RequestParam Integer countedQuantity,
            @Parameter(description = "Person who performed the count") @RequestParam(required = false) String countedBy) {
        return ResponseEntity.ok(inventoryService.performInventoryCount(id, countedQuantity, countedBy));
    }
}
