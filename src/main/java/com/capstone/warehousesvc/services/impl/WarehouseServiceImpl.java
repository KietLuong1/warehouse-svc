package com.capstone.warehousesvc.services.impl;

import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.WarehouseDTO;
import com.capstone.warehousesvc.exceptions.ResourceNotFoundException;
import com.capstone.warehousesvc.models.Product;
import com.capstone.warehousesvc.models.Warehouse;
import com.capstone.warehousesvc.repositories.ProductRepository;
import com.capstone.warehousesvc.repositories.WarehouseRepository;
import com.capstone.warehousesvc.services.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public WarehouseDTO createWarehouse(WarehouseDTO warehouseDTO) {
        // Add your implementation here
        Warehouse warehouse = modelMapper.map(warehouseDTO, Warehouse.class);
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return modelMapper.map(savedWarehouse, WarehouseDTO.class);
    }

    @Override
    public WarehouseDTO getWarehouse(String id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found with id: " + id));

        return modelMapper.map(warehouse, WarehouseDTO.class);
    }

    @Override
    public List<WarehouseDTO> getAllWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findAll();

        return warehouses.stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<WarehouseDTO> getActiveWarehouses() {
        List<Warehouse> warehouses = warehouseRepository.findByActive(true);

        return warehouses.stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public WarehouseDTO updateWarehouse(String id, WarehouseDTO warehouseDTO) {
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

    @Override
    public void deleteWarehouse(String id) {
        // Check if warehouse exists
        if (!warehouseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Warehouse not found with id: " + id);
        }

        warehouseRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> getWarehouseInventory(String warehouseId) {
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
