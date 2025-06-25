package com.capstone.warehousesvc.specification;

import com.capstone.warehousesvc.models.Inventory;
import com.capstone.warehousesvc.models.Product;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class InventoryFilter {
    public static Specification<Inventory> byKeyword(String keyword, String warehouseId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";

                Join<Object, Object> productJoin = root.join("product", JoinType.LEFT);

                List<Predicate> keywordPredicates = new ArrayList<>();
                keywordPredicates.add(cb.like(cb.lower(productJoin.get("name")), pattern));
                keywordPredicates.add(cb.like(cb.lower(productJoin.get("sku")), pattern));
                keywordPredicates.add(cb.like(cb.lower(productJoin.get("description")), pattern));

                keywordPredicates.add(cb.like(cb.lower(root.get("batchNumber")), pattern));
                keywordPredicates.add(cb.like(cb.lower(root.get("locationCode")), pattern));

                predicates.add(cb.or(keywordPredicates.toArray(new Predicate[0])));
            }

            if (warehouseId != null && !warehouseId.isEmpty()) {
                predicates.add(cb.equal(root.join("warehouse", JoinType.LEFT).get("id"), warehouseId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
