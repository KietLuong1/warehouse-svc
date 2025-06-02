package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.TransactionDTO;
import com.capstone.warehousesvc.dtos.TransactionRequest;
import com.capstone.warehousesvc.dtos.response.PagedResponse;
import com.capstone.warehousesvc.enums.TransactionStatus;

public interface TransactionService {
    Response purchase(TransactionRequest transactionRequest);

    Response sell(TransactionRequest transactionRequest);

    Response returnToSupplier(TransactionRequest transactionRequest);

    PagedResponse<TransactionDTO> getAllTransactions(int page, int size, String filter);

    Response getAllTransactionById(String id);

    PagedResponse<TransactionDTO> getAllTransactionByMonthAndYear(int month, int year, int page, int size);

    Response updateTransactionStatus(String transactionId, TransactionStatus status);
}
