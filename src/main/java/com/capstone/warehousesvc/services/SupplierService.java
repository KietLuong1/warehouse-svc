package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.SupplierDTO;

public interface SupplierService {

    Response addSupplier(SupplierDTO supplierDTO);

    Response updateSupplier(Long id, SupplierDTO supplierDTO);

    Response getAllSupplier();

    Response getSupplierById(Long id);

    Response deleteSupplier(Long id);

}
