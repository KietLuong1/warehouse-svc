package com.capstone.warehousesvc.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.capstone.warehousesvc.enums.TransactionStatus;
import com.capstone.warehousesvc.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Transaction details")
public class TransactionDTO {

    @Schema(description = "Transaction ID", example = "1")
    private Long id;

    @Schema(description = "Total number of products in transaction", example = "5")
    private Integer totalProducts;

    @Schema(description = "Total price of transaction", example = "1250.75")
    private BigDecimal totalPrice;

    @Schema(description = "Type of transaction: PURCHASE, SALE, RETURN", example = "PURCHASE")
    private TransactionType transactionType;

    @Schema(description = "Status of transaction: PENDING, PROCESSING, COMPLETED", example = "COMPLETED")
    private TransactionStatus status;

    @Schema(description = "Transaction description", example = "Weekly inventory restock")
    private String description;
    
    @Schema(description = "Additional transaction notes", example = "Delivered by John from ABC Suppliers")
    private String note;

    @Schema(description = "Transaction creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Transaction update timestamp")
    private LocalDateTime updateAt;

    @Schema(description = "Product involved in transaction")
    private ProductDTO product;

    @Schema(description = "User who created/processed transaction")
    private UserDTO user;

    @Schema(description = "Supplier for this transaction (for purchases/returns)")
    private SupplierDTO supplier;

    @Schema(description = "Order type: PURCHASE_ORDER, SALES_ORDER, TRANSFER_ORDER", example = "PURCHASE_ORDER")
    private String orderType;

    @Schema(description = "Order priority level", example = "HIGH")
    private String priority;
}
