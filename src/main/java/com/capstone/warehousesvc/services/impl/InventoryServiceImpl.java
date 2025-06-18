package com.capstone.warehousesvc.services.impl;

import com.capstone.warehousesvc.dtos.*;
import com.capstone.warehousesvc.exceptions.NotFoundException;
import com.capstone.warehousesvc.models.Inventory;
import com.capstone.warehousesvc.models.Product;
import com.capstone.warehousesvc.models.Warehouse;
import com.capstone.warehousesvc.repositories.InventoryRepository;
import com.capstone.warehousesvc.repositories.ProductRepository;
import com.capstone.warehousesvc.repositories.WarehouseRepository;
import com.capstone.warehousesvc.security.AuthUser;
import com.capstone.warehousesvc.services.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;
    private final ModelMapper modelMapper;

    private String getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof AuthUser) {
                AuthUser authUser = (AuthUser) authentication.getPrincipal();
                return authUser.getId();
            }
            return "SYSTEM";
        } catch (Exception e) {
            log.warn("Could not get current user, using SYSTEM", e);
            return "SYSTEM";
        }
    }

    @Override
    public Response createInventory(InventoryDTO inventoryDTO) {
        log.info("Creating inventory for product {} in warehouse {}",
                inventoryDTO.getProductId(), inventoryDTO.getWarehouseId());

        // Validate product exists
        Product product = productRepository.findById(inventoryDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));

        // Validate warehouse exists
        Warehouse warehouse = warehouseRepository.findById(inventoryDTO.getWarehouseId())
                .orElseThrow(() -> new NotFoundException("Warehouse not found"));

        // Check if inventory already exists for this product-warehouse combination
        if (inventoryRepository.existsByProductIdAndWarehouseId(
                inventoryDTO.getProductId(), inventoryDTO.getWarehouseId())) {
            throw new IllegalArgumentException("Inventory already exists for this product in this warehouse");
        }

        // Create inventory entity
        Inventory inventory = Inventory.builder()
                .product(product)
                .warehouse(warehouse)
                .quantityOnHand(inventoryDTO.getQuantityOnHand())
                .reservedQuantity(inventoryDTO.getReservedQuantity() != null ? inventoryDTO.getReservedQuantity() : 0)
                .reorderLevel(inventoryDTO.getReorderLevel())
                .maxStockLevel(inventoryDTO.getMaxStockLevel())
                .unitCost(inventoryDTO.getUnitCost())
                .locationCode(inventoryDTO.getLocationCode())
                .batchNumber(inventoryDTO.getBatchNumber())
                .expiryDate(inventoryDTO.getExpiryDate())
                .lastCountedDate(inventoryDTO.getLastCountedDate())
                .updatedBy(getCurrentUserId())
                .build();

        inventoryRepository.save(inventory);

        return Response.builder()
                .status(201)
                .message("Inventory created successfully")
                .build();
    }

    @Override
    public Response updateInventory(String id, InventoryDTO inventoryDTO) {
        log.info("Updating inventory with id: {}", id);

        Inventory existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        // Update fields if provided
        if (inventoryDTO.getQuantityOnHand() != null) {
            existingInventory.setQuantityOnHand(inventoryDTO.getQuantityOnHand());
        }
        if (inventoryDTO.getReservedQuantity() != null) {
            existingInventory.setReservedQuantity(inventoryDTO.getReservedQuantity());
        }
        if (inventoryDTO.getReorderLevel() != null) {
            existingInventory.setReorderLevel(inventoryDTO.getReorderLevel());
        }
        if (inventoryDTO.getMaxStockLevel() != null) {
            existingInventory.setMaxStockLevel(inventoryDTO.getMaxStockLevel());
        }
        if (inventoryDTO.getUnitCost() != null) {
            existingInventory.setUnitCost(inventoryDTO.getUnitCost());
        }
        if (inventoryDTO.getLocationCode() != null) {
            existingInventory.setLocationCode(inventoryDTO.getLocationCode());
        }
        if (inventoryDTO.getBatchNumber() != null) {
            existingInventory.setBatchNumber(inventoryDTO.getBatchNumber());
        }
        if (inventoryDTO.getExpiryDate() != null) {
            existingInventory.setExpiryDate(inventoryDTO.getExpiryDate());
        }
        if (inventoryDTO.getLastCountedDate() != null) {
            existingInventory.setLastCountedDate(inventoryDTO.getLastCountedDate());
        }

        existingInventory.setUpdatedBy(getCurrentUserId());
        inventoryRepository.save(existingInventory);

        return Response.builder()
                .status(200)
                .message("Inventory updated successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getInventoryById(String id) {
        log.info("Fetching inventory with id: {}", id);

        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        InventoryDTO inventoryDTO = mapToInventoryDTO(inventory);

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("inventory", inventoryDTO))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getAllInventory(int page, int size) {
        log.info("Fetching all inventory");
        Sort sort = Sort.by("lastUpdated").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Inventory> inventories = inventoryRepository.findAll(pageable);

        List<InventoryDTO> inventoryDTOs = inventories.stream()
                .map(this::mapToInventoryDTO)
                .toList();

        return Response.builder()
                .pageSize(pageable.getPageSize())
                .currentPage(page)
                .totalElements(inventories.getTotalElements())
                .totalPages(inventories.getTotalPages()).status(200)
                .message("Success")
                .data(inventoryDTOs)
                .build();
    }

    @Override
    public Response deleteInventory(String id) {
        log.info("Deleting inventory with id: {}", id);

        if (!inventoryRepository.existsById(id)) {
            throw new NotFoundException("Inventory not found");
        }

        inventoryRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Inventory deleted successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response searchInventory(String searchTerm) {
        log.info("Searching inventory with term: {}", searchTerm);

        List<Inventory> inventories = inventoryRepository.searchInventory(searchTerm);

        List<InventoryDTO> inventoryDTOs = inventories.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("inventories", inventoryDTOs))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getInventoryByProduct(String productId) {
        log.info("Fetching inventory for product: {}", productId);

        List<Inventory> inventories = inventoryRepository.findByProductId(productId);

        List<InventoryDTO> inventoryDTOs = inventories.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("inventories", inventoryDTOs))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getInventoryByWarehouse(String warehouseId) {
        log.info("Fetching inventory for warehouse: {}", warehouseId);

        List<Inventory> inventories = inventoryRepository.findByWarehouseId(warehouseId);

        List<InventoryDTO> inventoryDTOs = inventories.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("inventories", inventoryDTOs))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getInventoryByProductAndWarehouse(String productId, String warehouseId) {
        log.info("Fetching inventory for product {} in warehouse {}", productId, warehouseId);

        Inventory inventory = inventoryRepository.findByProductIdAndWarehouseId(productId, warehouseId)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        InventoryDTO inventoryDTO = mapToInventoryDTO(inventory);

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("inventory", inventoryDTO))
                .build();
    }

    @Override
    public Response adjustInventory(InventoryAdjustmentRequest request) {
        log.info("Adjusting inventory {} by {} with type {}",
                request.getInventoryId(), request.getQuantity(), request.getAdjustmentType());

        Inventory inventory = inventoryRepository.findById(request.getInventoryId())
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        int currentQuantity = inventory.getQuantityOnHand();
        int newQuantity;

        switch (request.getAdjustmentType().toUpperCase()) {
            case "ADD":
                newQuantity = currentQuantity + request.getQuantity();
                break;
            case "SUBTRACT":
                newQuantity = Math.max(0, currentQuantity - request.getQuantity());
                break;
            case "SET":
                newQuantity = request.getQuantity();
                break;
            default:
                throw new IllegalArgumentException("Invalid adjustment type: " + request.getAdjustmentType());
        }

        inventory.setQuantityOnHand(newQuantity);
        inventory.setUpdatedBy(getCurrentUserId());
        inventoryRepository.save(inventory);

        return Response.builder()
                .status(200)
                .message("Inventory adjusted successfully")
                .dataList(java.util.Map.of(
                        "previousQuantity", currentQuantity,
                        "newQuantity", newQuantity,
                        "adjustment", request.getQuantity(),
                        "type", request.getAdjustmentType()
                ))
                .build();
    }

    @Override
    public Response moveInventory(InventoryMovementRequest request) {
        log.info("Moving inventory from warehouse {} to warehouse {} for product {}",
                request.getFromWarehouseId(), request.getToWarehouseId(), request.getProductId());

        // Find source inventory
        Inventory sourceInventory = inventoryRepository.findByProductIdAndWarehouseId(
                        request.getProductId(), request.getFromWarehouseId())
                .orElseThrow(() -> new NotFoundException("Source inventory not found"));

        // Check if there's enough stock
        if (sourceInventory.getQuantityOnHand() < request.getQuantity()) {
            throw new IllegalArgumentException("Insufficient stock for movement");
        }

        // Update source inventory
        sourceInventory.setQuantityOnHand(sourceInventory.getQuantityOnHand() - request.getQuantity());
        sourceInventory.setUpdatedBy(getCurrentUserId());

        // Find or create destination inventory
        Inventory destinationInventory = inventoryRepository.findByProductIdAndWarehouseId(
                request.getProductId(), request.getToWarehouseId()).orElse(null);

        if (destinationInventory == null) {
            // Create new inventory at destination
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found"));
            Warehouse warehouse = warehouseRepository.findById(request.getToWarehouseId())
                    .orElseThrow(() -> new NotFoundException("Destination warehouse not found"));

            destinationInventory = Inventory.builder()
                    .product(product)
                    .warehouse(warehouse)
                    .quantityOnHand(request.getQuantity())
                    .reservedQuantity(0)
                    .unitCost(sourceInventory.getUnitCost())
                    .updatedBy(getCurrentUserId())
                    .build();
        } else {
            // Update existing inventory
            destinationInventory.setQuantityOnHand(destinationInventory.getQuantityOnHand() + request.getQuantity());
            destinationInventory.setUpdatedBy(getCurrentUserId());
        }

        inventoryRepository.save(sourceInventory);
        inventoryRepository.save(destinationInventory);

        return Response.builder()
                .status(200)
                .message("Inventory moved successfully")
                .dataList(java.util.Map.of(
                        "movedQuantity", request.getQuantity(),
                        "fromWarehouse", request.getFromWarehouseId(),
                        "toWarehouse", request.getToWarehouseId()
                ))
                .build();
    }

    @Override
    public Response reserveInventory(String inventoryId, Integer quantity) {
        log.info("Reserving {} units for inventory {}", quantity, inventoryId);

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        int availableQuantity = inventory.getQuantityOnHand() - inventory.getReservedQuantity();
        if (availableQuantity < quantity) {
            throw new IllegalArgumentException("Insufficient available stock for reservation");
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() + quantity);
        inventory.setUpdatedBy(getCurrentUserId());
        inventoryRepository.save(inventory);

        return Response.builder()
                .status(200)
                .message("Inventory reserved successfully")
                .dataList(java.util.Map.of(
                        "reservedQuantity", quantity,
                        "totalReserved", inventory.getReservedQuantity(),
                        "availableQuantity", inventory.getQuantityOnHand() - inventory.getReservedQuantity()
                ))
                .build();
    }

    @Override
    public Response releaseReservation(String inventoryId, Integer quantity) {
        log.info("Releasing {} reserved units for inventory {}", quantity, inventoryId);

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        if (inventory.getReservedQuantity() < quantity) {
            throw new IllegalArgumentException("Cannot release more than reserved quantity");
        }

        inventory.setReservedQuantity(inventory.getReservedQuantity() - quantity);
        inventory.setUpdatedBy(getCurrentUserId());
        inventoryRepository.save(inventory);

        return Response.builder()
                .status(200)
                .message("Reservation released successfully")
                .dataList(java.util.Map.of(
                        "releasedQuantity", quantity,
                        "totalReserved", inventory.getReservedQuantity(),
                        "availableQuantity", inventory.getQuantityOnHand() - inventory.getReservedQuantity()
                ))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getInventorySummary() {
        log.info("Generating inventory summary");

        InventorySummary summary = InventorySummary.builder()
                .totalInventoryItems(inventoryRepository.count())
                .totalInventoryValue(inventoryRepository.getTotalInventoryValue())
                .lowStockCount(inventoryRepository.countLowStockItems())
                .overstockCount(inventoryRepository.countOverstockItems())
                .outOfStockCount(inventoryRepository.countByQuantityOnHand(0))
                .expiringSoonCount(inventoryRepository.countItemsExpiringSoon(LocalDateTime.now().plusDays(30)))
                .build();

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("summary", summary))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getLowStockItems() {
        log.info("Fetching low stock items");

        List<Inventory> lowStockItems = inventoryRepository.findLowStockItems();
        List<InventoryDTO> inventoryDTOs = lowStockItems.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("lowStockItems", inventoryDTOs))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getOverstockItems() {
        log.info("Fetching overstock items");

        List<Inventory> overstockItems = inventoryRepository.findOverstockItems();
        List<InventoryDTO> inventoryDTOs = overstockItems.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("overstockItems", inventoryDTOs))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getOutOfStockItems() {
        log.info("Fetching out of stock items");

        List<Inventory> outOfStockItems = inventoryRepository.findByQuantityOnHand(0);
        List<InventoryDTO> inventoryDTOs = outOfStockItems.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("outOfStockItems", inventoryDTOs))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getExpiringItems(Integer daysAhead) {
        log.info("Fetching items expiring in {} days", daysAhead);

        LocalDateTime expiryDate = LocalDateTime.now().plusDays(daysAhead);
        List<Inventory> expiringItems = inventoryRepository.findItemsExpiringSoon(expiryDate);
        List<InventoryDTO> inventoryDTOs = expiringItems.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("expiringItems", inventoryDTOs))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getTopInventoryItemsByValue(Integer limit) {
        log.info("Fetching top {} inventory items by value", limit);

        Pageable pageable = PageRequest.of(0, limit);
        List<Inventory> topItems = inventoryRepository.findTopInventoryItemsByValue();

        List<InventoryDTO> inventoryDTOs = topItems.stream()
                .limit(limit)
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("topInventoryItems", inventoryDTOs))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getInventoryByLocation(String locationCode) {
        log.info("Fetching inventory for location: {}", locationCode);

        List<Inventory> inventories = inventoryRepository.findByLocationCode(locationCode);
        List<InventoryDTO> inventoryDTOs = inventories.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("inventories", inventoryDTOs))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getInventoryByBatch(String batchNumber) {
        log.info("Fetching inventory for batch: {}", batchNumber);

        List<Inventory> inventories = inventoryRepository.findByBatchNumber(batchNumber);
        List<InventoryDTO> inventoryDTOs = inventories.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("inventories", inventoryDTOs))
                .build();
    }

    @Override
    public Response updateInventoryLocation(String inventoryId, String locationCode) {
        log.info("Updating location for inventory {} to {}", inventoryId, locationCode);

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        inventory.setLocationCode(locationCode);
        inventory.setUpdatedBy(getCurrentUserId());
        inventoryRepository.save(inventory);

        return Response.builder()
                .status(200)
                .message("Inventory location updated successfully")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public Response getItemsNeedingCount(Integer daysSinceLastCount) {
        log.info("Fetching items needing count (not counted in {} days)", daysSinceLastCount);

        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysSinceLastCount);
        List<Inventory> items = inventoryRepository.findItemsNeedingCount(cutoffDate);
        List<InventoryDTO> inventoryDTOs = items.stream()
                .map(this::mapToInventoryDTO)
                .collect(Collectors.toList());

        return Response.builder()
                .status(200)
                .message("Success")
                .dataList(java.util.Map.of("itemsNeedingCount", inventoryDTOs))
                .build();
    }

    @Override
    public Response updateLastCountedDate(String inventoryId) {
        log.info("Updating last counted date for inventory: {}", inventoryId);

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        inventory.setLastCountedDate(LocalDateTime.now());
        inventory.setUpdatedBy(getCurrentUserId());
        inventoryRepository.save(inventory);

        return Response.builder()
                .status(200)
                .message("Last counted date updated successfully")
                .build();
    }

    @Override
    public Response performInventoryCount(String inventoryId, Integer countedQuantity, String countedBy) {
        log.info("Performing inventory count for {} with counted quantity: {}", inventoryId, countedQuantity);

        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new NotFoundException("Inventory not found"));

        Integer previousQuantity = inventory.getQuantityOnHand();
        Integer variance = countedQuantity - previousQuantity;

        inventory.setQuantityOnHand(countedQuantity);
        inventory.setLastCountedDate(LocalDateTime.now());
        inventory.setUpdatedBy(countedBy != null ? countedBy : getCurrentUserId());
        inventoryRepository.save(inventory);

        return Response.builder()
                .status(200)
                .message("Inventory count completed successfully")
                .dataList(java.util.Map.of(
                        "previousQuantity", previousQuantity,
                        "countedQuantity", countedQuantity,
                        "variance", variance,
                        "countedBy", countedBy != null ? countedBy : getCurrentUserId(),
                        "countedDate", LocalDateTime.now()
                ))
                .build();
    }

    /**
     * Helper method to map Inventory entity to InventoryDTO with computed fields
     */
    private InventoryDTO mapToInventoryDTO(Inventory inventory) {
        InventoryDTO dto = modelMapper.map(inventory, InventoryDTO.class);

        // Set computed fields
        dto.setAvailableQuantity(inventory.getAvailableQuantity());
        dto.setIsLowStock(inventory.isLowStock());
        dto.setIsOverstock(inventory.isOverstock());
        dto.setTotalValue(inventory.getTotalValue());

        // Map product and warehouse details
        if (inventory.getProduct() != null) {
            dto.setProduct(modelMapper.map(inventory.getProduct(), ProductDTO.class));
        }
        if (inventory.getWarehouse() != null) {
            dto.setWarehouse(modelMapper.map(inventory.getWarehouse(), WarehouseDTO.class));
        }

        return dto;
    }
}
