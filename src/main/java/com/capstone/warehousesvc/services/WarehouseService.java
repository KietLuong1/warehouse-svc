package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.WarehouseDTO;

import java.util.List;

public interface WarehouseService {
    
    WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO);
    
    WarehouseDTO getWarehouse(Long id);
    
    List<WarehouseDTO> getAllWarehouses();
    
    List<WarehouseDTO> getActiveWarehouses();
    
    WarehouseDTO updateWarehouse(Long id, WarehouseDTO warehouseDTO);
    
    void deleteWarehouse(Long id);
    
    List<ProductDTO> getWarehouseInventory(Long warehouseId);
}