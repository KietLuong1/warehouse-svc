
package com.capstone.warehousesvc.repositories;

import com.capstone.warehousesvc.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {
    Page<Product> findByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);

    List<Product> findByStockQuantityLessThan(Integer threshold);
}