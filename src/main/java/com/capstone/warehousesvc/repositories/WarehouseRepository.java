package com.capstone.warehousesvc.repositories;

import com.capstone.warehousesvc.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    List<Warehouse> findByActive(Boolean active);
    boolean existsByName(String name);
} 