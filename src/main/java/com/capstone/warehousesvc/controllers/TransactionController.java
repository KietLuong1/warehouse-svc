package com.capstone.warehousesvc.controllers;

import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.TransactionRequest;
import com.capstone.warehousesvc.enums.TransactionStatus;
import com.capstone.warehousesvc.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Transactions", description = "Inventory transactions management")
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Purchase inventory", description = "Record new inventory purchase transaction")
    @PostMapping("/purchase")
    public ResponseEntity<Response> purchaseInventory(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.purchase(transactionRequest));
    }

    @Operation(summary = "Sell inventory", description = "Record inventory sale transaction")
    @PostMapping("/sell")
    public ResponseEntity<Response> makeSale(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.sell(transactionRequest));
    }

    @Operation(summary = "Return to supplier", description = "Record inventory return to supplier")
    @PostMapping("/return")
    public ResponseEntity<Response> returnToSupplier(@RequestBody @Valid TransactionRequest transactionRequest) {
        return ResponseEntity.ok(transactionService.returnToSupplier(transactionRequest));
    }

    @Operation(summary = "Get all transactions", description = "Get paginated list of transactions with optional filtering")
    @GetMapping("/all")
    public ResponseEntity<Response> getAllTransactions(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "1000") int size,
            @Parameter(description = "Filter criteria") @RequestParam(required = false) String filter) {
        return ResponseEntity.ok(transactionService.getAllTransactions(page, size, filter));
    }

    @Operation(summary = "Get transaction by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getAllTransactionById(id));
    }

    @Operation(summary = "Get transactions by month/year", 
               description = "Filter transactions by month and year")
    @GetMapping("/by-month-year")
    public ResponseEntity<Response> getTransactionByMonthAndYear(
            @Parameter(name = "month", description = "Month (1-12)", example = "3") @RequestParam int month,
            @Parameter(name = "year", description = "Year (4 digits)", example = "2024") @RequestParam int year) {
        return ResponseEntity.ok(transactionService.getAllTransactionByMonthAndYear(month, year));
    }

    @Operation(summary = "Update transaction status", description = "Update the status of an existing transaction")
    @PutMapping("/{transactionId}")
    public ResponseEntity<Response> updateTransactionStatus(
            @Parameter(description = "Transaction ID") @PathVariable Long transactionId,
            @Parameter(description = "New transaction status") @RequestBody TransactionStatus status) {
        return ResponseEntity.ok(transactionService.updateTransactionStatus(transactionId, status));
    }
}
