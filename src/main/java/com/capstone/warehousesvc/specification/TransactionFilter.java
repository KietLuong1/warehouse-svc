package com.capstone.warehousesvc.specification;

import com.capstone.warehousesvc.enums.TransactionStatus;
import com.capstone.warehousesvc.enums.TransactionType;
import com.capstone.warehousesvc.models.Product;
import com.capstone.warehousesvc.models.Transaction;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Specification is used in Filtering data in a database
public class TransactionFilter {

    public static Specification<Transaction> byFilter(String filter, String status, String transactionType) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filter != null && !filter.isEmpty()) {
                String searchPattern = "%" + filter.toLowerCase() + "%";
                List<Predicate> keywordPredicates = new ArrayList<>();

                keywordPredicates.add(cb.like(cb.lower(root.get("description")), searchPattern));
                keywordPredicates.add(cb.like(cb.lower(root.get("note")), searchPattern));
                keywordPredicates.add(cb.like(cb.lower(root.get("status").as(String.class)), searchPattern));
                keywordPredicates.add(cb.like(cb.lower(root.get("transactionType").as(String.class)), searchPattern));

                keywordPredicates.add(cb.like(cb.lower(root.get("username")), searchPattern));
                keywordPredicates.add(cb.like(cb.lower(root.get("userId")), searchPattern));

                Join<?, ?> supplierJoin = root.join("supplier", JoinType.LEFT);
                keywordPredicates.add(cb.like(cb.lower(supplierJoin.get("name")), searchPattern));
                keywordPredicates.add(cb.like(cb.lower(supplierJoin.get("contactInfo")), searchPattern));

                Join<?, ?> productJoin = root.join("product", JoinType.LEFT);
                keywordPredicates.add(cb.like(cb.lower(productJoin.get("name")), searchPattern));
                keywordPredicates.add(cb.like(cb.lower(productJoin.get("sku")), searchPattern));
                keywordPredicates.add(cb.like(cb.lower(productJoin.get("description")), searchPattern));

                Join<?, ?> categoryJoin = productJoin.join("category", JoinType.LEFT);
                keywordPredicates.add(cb.like(cb.lower(categoryJoin.get("name")), searchPattern));

                predicates.add(cb.or(keywordPredicates.toArray(new Predicate[0])));
            }

            if (status != null && !status.isEmpty()) {
                predicates.add(cb.equal(root.get("status").as(String.class), status));
            }

            if (transactionType != null && !transactionType.isEmpty()) {
                predicates.add(cb.equal(root.get("transactionType").as(String.class), transactionType));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // New method for filtering transactions by month and year
    public static Specification<Transaction> byMonthAndYear(int month, int year) {
        return (root, query, criteriaBuilder) -> {
            // Use the month and year functions on the createdAt date field
            Expression<Integer> monthExpression = criteriaBuilder.function("month", Integer.class, root.get("createdAt"));
            Expression<Integer> yearExpression = criteriaBuilder.function("year", Integer.class, root.get("createdAt"));

            // Create predicates for the month and year
            Predicate monthPredicate = criteriaBuilder.equal(monthExpression, month);
            Predicate yearPredicate = criteriaBuilder.equal(yearExpression, year);

            // Combine the month and year predicates
            return criteriaBuilder.and(monthPredicate, yearPredicate);
        };
    }


    public static Specification<Transaction> byProductName(String productName) {
        return (root, query, criteriaBuilder) -> {
            if (productName == null || productName.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            Join<Transaction, Product> productJoin = root.join("product", JoinType.INNER);

            String searchPattern = "%" + productName.toLowerCase() + "%";

            return criteriaBuilder.like(
                    criteriaBuilder.lower(productJoin.get("name")),
                    searchPattern
            );
        };
    }

}