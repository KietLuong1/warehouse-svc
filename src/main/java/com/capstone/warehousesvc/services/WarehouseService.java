package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.WarehouseDTO;
import com.capstone.warehousesvc.exceptions.ResourceAlreadyExistsException;
import com.capstone.warehousesvc.exceptions.ResourceNotFoundException;
import com.capstone.warehousesvc.models.Product;
import com.capstone.warehousesvc.models.Warehouse;
import com.capstone.warehousesvc.repositories.ProductRepository;
import com.capstone.warehousesvc.repositories.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    
    public WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        // Check if warehouse with same name already exists
        if (warehouseRepository.existsByName(warehouseDTO.getName())) {
            throw new ResourceAlreadyExistsException("Warehouse with name " + warehouseDTO.getName() + " already exists");
        }
        
        // Map DTO to entity
        Warehouse warehouse = modelMapper.map(warehouseDTO, Warehouse.class);
        
        // Save and return
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return modelMapper.map(savedWarehouse, WarehouseDTO.class);
    }
    
    public WarehouseDTO getWarehouse(Long id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        
        return modelMapper.map(warehouse, WarehouseDTO.class);
    }
    
    public List<WarehouseDTO> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findAll();
        
        return warehouses.stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDTO.class))
                .collect(Collectors.toList());
    }
    
    public List<WarehouseDTO> getActiveWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findByActive(true);
        
        return warehouses.stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDTO.class))
                .collect(Collectors.toList());
    }
    
    public WarehouseDTO updateWarehouse(Long id, WarehouseDTO warehouseDTO) {
        // Check if warehouse exists
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));
        
        // Update fields
        warehouse.setName(warehouseDTO.getName());
        warehouse.setLocation(warehouseDTO.getLocation());
        warehouse.setCapacity(warehouseDTO.getCapacity());
        warehouse.setActive(warehouseDTO.getActive());
        
        // Save and return
        Warehouse updatedWarehouse = warehouseRepository.save(warehouse);
        return modelMapper.map(updatedWarehouse, WarehouseDTO.class);
    }
    
    public void deleteWarehouse(Long id) {
        // Check if warehouse exists
        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + id);
        }
        
        warehouseRepository.deleteById(id);
    }
    
    public List<ProductDTO> getWarehouseInventory(Long warehouseId) {
        // Check if warehouse exists
        if (!warehouseRepository.existsById(warehouseId)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + warehouseId);
        }
        
        // In a real application, you would fetch products associated with this warehouse
        // For now, as a placeholder, return all products
        List<Product> products = productRepository.findAll();
        
        return products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
    }
} 