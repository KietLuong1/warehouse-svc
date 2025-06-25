package com.capstone.warehousesvc.specification;

import com.capstone.warehousesvc.models.Product;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductFilter {
    public static Specification<Product> byKeyword(String keyword, String categoryId, String warehouseId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (keyword != null && !keyword.isEmpty()) {
                String pattern = "%" + keyword.toLowerCase() + "%";
                List<Predicate> keywordPredicates = new ArrayList<>();
                keywordPredicates.add(cb.like(cb.lower(root.get("name")), pattern));
                keywordPredicates.add(cb.like(cb.lower(root.get("sku")), pattern));
                keywordPredicates.add(cb.like(cb.lower(root.get("description")), pattern));
                predicates.add(cb.or(keywordPredicates.toArray(new Predicate[0])));
            }

            if (categoryId != null && !categoryId.isEmpty()) {
                predicates.add(cb.equal(root.join("category", JoinType.LEFT).get("id"), categoryId));
            }

            if (warehouseId != null && !warehouseId.isEmpty()) {
                predicates.add(cb.equal(root.join("warehouse", JoinType.LEFT).get("id"), warehouseId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
