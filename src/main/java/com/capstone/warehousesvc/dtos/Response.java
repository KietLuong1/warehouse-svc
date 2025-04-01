package com.capstone.warehousesvc.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.capstone.warehousesvc.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standard API response wrapper")
public class Response {

    @Schema(description = "HTTP status code", example = "200")
    private int status;
    
    @Schema(description = "Response message", example = "Operation completed successfully")
    private String message;
    
    @Schema(description = "Authentication token (for auth endpoints)", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;
    
    @Schema(description = "User role (for auth endpoints)", example = "ADMIN")
    private UserRole role;
    
    @Schema(description = "Token expiration time", example = "2024-04-01T15:30:45")
    private String expirationTime;

    @Schema(description = "Total pages in paginated response", example = "5")
    private Integer totalPages;
    
    @Schema(description = "Total elements count in paginated response", example = "42")
    private Long totalElements;

    @Schema(description = "User data (when returning single user)")
    private UserDTO user;
    
    @Schema(description = "Users list (when returning multiple users)")
    private List<UserDTO> users;

    @Schema(description = "Supplier data (when returning single supplier)")
    private SupplierDTO supplier;
    
    @Schema(description = "Suppliers list (when returning multiple suppliers)")
    private List<SupplierDTO> suppliers;

    @Schema(description = "Category data (when returning single category)")
    private CategoryDTO category;
    
    @Schema(description = "Categories list (when returning multiple categories)")
    private List<CategoryDTO> categories;

    @Schema(description = "Product data (when returning single product)")
    private ProductDTO product;
    
    @Schema(description = "Products list (when returning multiple products)")
    private List<ProductDTO> products;

    @Schema(description = "Transaction data (when returning single transaction)")
    private TransactionDTO transaction;
    
    @Schema(description = "Transactions list (when returning multiple transactions)")
    private List<TransactionDTO> transactions;

    @Schema(description = "Generic data map for flexible responses")
    private Map<String, Object> data;

    @Schema(description = "Response timestamp", example = "2024-04-01T12:34:56")
    private final LocalDateTime timestamp = LocalDateTime.now();
}
