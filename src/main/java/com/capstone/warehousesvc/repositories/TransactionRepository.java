
package com.capstone.warehousesvc.repositories;

import com.capstone.warehousesvc.models.Transaction;
import com.capstone.warehousesvc.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByCreatedAtBetweenAndTransactionType(LocalDateTime startDate, LocalDateTime endDate, TransactionType transactionType);

    List<Transaction> findTop10ByOrderByCreatedAtDesc();
}