
package com.capstone.warehousesvc.repositories;

import com.capstone.warehousesvc.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    Page<Product> findByNameContainingOrDescriptionContaining(String name, String description, Pageable pageable);

    List<Product> findByStockQuantityLessThan(Integer threshold);
}