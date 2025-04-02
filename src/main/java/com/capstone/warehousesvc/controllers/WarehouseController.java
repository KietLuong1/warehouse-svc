package com.capstone.warehousesvc.controllers;

import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.WarehouseDTO;
import com.capstone.warehousesvc.services.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Warehouse", description = "Warehouse location management")
@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    
    private final WarehouseService warehouseService;
    
    @Operation(summary = "Create warehouse location")
    @PostMapping
    public ResponseEntity<Response> createWarehouse(@Valid @RequestBody WarehouseDTO dto) {
        WarehouseDTO createdWarehouse = warehouseService.createWarehouse(dto);
        
        Map<String, Object> data = new HashMap<>();
        data.put("warehouse", createdWarehouse);
        
        Response response = Response.builder()
                .status(HttpStatus.CREATED.value())
                .message("Warehouse created successfully")
                .data(data)
                .build();
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @Operation(summary = "Get inventory by location")
    @GetMapping("/{id}/inventory")
    public ResponseEntity<Response> getLocationInventory(@PathVariable Long id) {
        Map<String, Object> data = new HashMap<>();
        data.put("inventory", warehouseService.getWarehouseInventory(id));
        
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Warehouse inventory retrieved successfully")
                .data(data)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get all warehouses")
    @GetMapping
    public ResponseEntity<Response> getAllWarehouses() {
        Map<String, Object> data = new HashMap<>();
        data.put("warehouses", warehouseService.getAllWarehouses());
        
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Warehouses retrieved successfully")
                .data(data)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get active warehouses")
    @GetMapping("/active")
    public ResponseEntity<Response> getActiveWarehouses() {
        Map<String, Object> data = new HashMap<>();
        data.put("warehouses", warehouseService.getActiveWarehouses());
        
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Active warehouses retrieved successfully")
                .data(data)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Get warehouse by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getWarehouse(@PathVariable Long id) {
        Map<String, Object> data = new HashMap<>();
        data.put("warehouse", warehouseService.getWarehouse(id));
        
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Warehouse retrieved successfully")
                .data(data)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Update warehouse")
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateWarehouse(@PathVariable Long id, @Valid @RequestBody WarehouseDTO dto) {
        WarehouseDTO updatedWarehouse = warehouseService.updateWarehouse(id, dto);
        
        Map<String, Object> data = new HashMap<>();
        data.put("warehouse", updatedWarehouse);
        
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Warehouse updated successfully")
                .data(data)
                .build();
        
        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Delete warehouse")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        
        Response response = Response.builder()
                .status(HttpStatus.OK.value())
                .message("Warehouse deleted successfully")
                .build();
        
        return ResponseEntity.ok(response);
    }
} 