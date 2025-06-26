package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.WarehouseDTO;

import java.util.List;

public interface WarehouseService {
    
    WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO);
    
    WarehouseDTO getWarehouse(String id);

    Response getAllWarehouses(int page, int size, String keyword);

    List<WarehouseDTO> getActiveWarehouses();

    WarehouseDTO updateWarehouse(String id, WarehouseDTO warehouseDTO);

    void deleteWarehouse(String id);

    List<ProductDTO> getWarehouseInventory(String warehouseId);
}