package com.capstone.warehousesvc.services.impl;

import com.capstone.warehousesvc.dtos.InventoryDTO;
import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.WarehouseDTO;
import com.capstone.warehousesvc.exceptions.ResourceNotFoundException;
import com.capstone.warehousesvc.models.Inventory;
import com.capstone.warehousesvc.models.Product;
import com.capstone.warehousesvc.models.Warehouse;
import com.capstone.warehousesvc.repositories.ProductRepository;
import com.capstone.warehousesvc.repositories.WarehouseRepository;
import com.capstone.warehousesvc.services.WarehouseService;
import com.capstone.warehousesvc.specification.InventoryFilter;
import com.capstone.warehousesvc.specification.ProductFilter;
import com.capstone.warehousesvc.specification.WarehouseFilter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
//    @Transactional(readOnly = true)
    public Response getAllWarehouses(int page, int size, String keyword) {
        Sort sort = Sort.by("createdAt").descending();
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Specification<Warehouse> spec = WarehouseFilter.byKeyword(keyword);

        Page<Warehouse> warehouses = warehouseRepository.findAll(spec, pageable);

        List<WarehouseDTO> warehouseDTOS = warehouses.stream()
                .map(warehouse -> modelMapper.map(warehouse, WarehouseDTO.class))
                .collect(Collectors.toList());

        return Response.builder()
                .pageSize(pageable.getPageSize())
                .currentPage(page)
                .totalElements(warehouses.getTotalElements())
                .totalPages(warehouses.getTotalPages())
                .message("Success")
                .data(warehouseDTOS)
                .status(200)
                .build();
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
