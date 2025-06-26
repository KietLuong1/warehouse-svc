package com.capstone.warehousesvc.services.impl;


import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.SupplierDTO;
import com.capstone.warehousesvc.exceptions.NotFoundException;
import com.capstone.warehousesvc.models.Product;
import com.capstone.warehousesvc.models.Supplier;
import com.capstone.warehousesvc.repositories.SupplierRepository;
import com.capstone.warehousesvc.services.SupplierService;
import com.capstone.warehousesvc.specification.ProductFilter;
import com.capstone.warehousesvc.specification.SupplierFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final ModelMapper modelMapper;


    @Override
    public Response addSupplier(SupplierDTO supplierDTO) {

        Supplier supplierToSave = modelMapper.map(supplierDTO, Supplier.class);

        supplierRepository.save(supplierToSave);

        return Response.builder()
                .status(200)
                .message("Supplier Saved Successfully")
                .build();
    }

    @Override
    public Response updateSupplier(String id, SupplierDTO supplierDTO) {

        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        if (supplierDTO.getName() != null) existingSupplier.setName(supplierDTO.getName());
        if (supplierDTO.getContactInfo() != null) existingSupplier.setContactInfo(supplierDTO.getContactInfo());
        if (supplierDTO.getAddress() != null) existingSupplier.setAddress(supplierDTO.getAddress());

        supplierRepository.save(existingSupplier);

        return Response.builder()
                .status(200)
                .message("Supplier Was Successfully Updated")
                .build();
    }

    @Override
    public Response getAllSupplier(int page, int size, String keyword) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Specification<Supplier> spec = SupplierFilter.byKeyword(keyword);
        Page<Supplier> pageResult = supplierRepository.findAll(spec, pageable);

        List<SupplierDTO> suppliers = modelMapper.map(
                pageResult.getContent(),
                new TypeToken<List<SupplierDTO>>() {}.getType()
        );

        return Response.builder()
                .status(200)
                .currentPage(page)
                .pageSize(size)
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .message("success")
                .suppliers(suppliers)
                .build();
    }

    @Override
    public Response getSupplierById(String id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        SupplierDTO supplierDTO = modelMapper.map(supplier, SupplierDTO.class);

        return Response.builder()
                .status(200)
                .message("success")
                .supplier(supplierDTO)
                .build();
    }

    @Override
    public Response deleteSupplier(String id) {

        supplierRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Supplier Not Found"));

        supplierRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Supplier Was Successfully Deleted")
                .build();
    }
}
