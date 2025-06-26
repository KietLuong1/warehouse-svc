package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.SupplierDTO;

public interface SupplierService {

    Response addSupplier(SupplierDTO supplierDTO);

    Response updateSupplier(String id, SupplierDTO supplierDTO);

    Response getAllSupplier(int page, int size, String keyword);

    Response getSupplierById(String id);

    Response deleteSupplier(String id);

}
