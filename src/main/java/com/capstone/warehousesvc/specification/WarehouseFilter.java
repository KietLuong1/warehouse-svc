package com.capstone.warehousesvc.specification;

import com.capstone.warehousesvc.models.Supplier;
import com.capstone.warehousesvc.models.Warehouse;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class WarehouseFilter {
    public static Specification<Warehouse> byKeyword(String keyword) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isBlank()) {
                String pattern = "%" + keyword.toLowerCase() + "%";

                List<Predicate> keywordPredicates = new ArrayList<>();
//                keywordPredicates.add(cb.like(cb.lower(root.get("name")), pattern));
                keywordPredicates.add(cb.like(cb.lower(root.get("location")), pattern));
                keywordPredicates.add(cb.like(cb.lower(root.get("capacity")), pattern));

                predicates.add(cb.or(keywordPredicates.toArray(new Predicate[0])));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
