package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.TransactionRequest;
import com.capstone.warehousesvc.enums.TransactionStatus;

public interface TransactionService {
    Response purchase(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    Response getAllTransactions(int page, int size, String filter, String status, String transactionType);

    Response getAllTransactionById(String id);

    Response getAllTransactionByMonthAndYear(int month, int year);

    Response updateTransactionStatus(String transactionId, TransactionStatus status);

    Response searchTransactionsByProductName(String productName, int page, int size);
}
