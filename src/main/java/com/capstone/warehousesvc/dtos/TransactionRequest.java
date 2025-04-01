package com.capstone.warehousesvc.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionRequest {

    @Schema(description = "Product ID", example = "123")
    @Positive(message = "product id is required")
    private Long productId;

    @Schema(description = "Transaction quantity", example = "10", minimum = "1")
    @Positive(message = "quantity id is required")
    private Integer quantity;

    @Schema(description = "Supplier ID (required for purchases)", example = "456")
    private Long supplierId;

    private String description;

    private String note;


}
