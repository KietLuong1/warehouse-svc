package com.capstone.warehousesvc.repositories;

import com.capstone.warehousesvc.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
