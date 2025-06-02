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

    @Schema(description = "Product ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String productId;

    @Schema(description = "Transaction quantity", example = "10", minimum = "1")
    @Positive(message = "quantity id is required")
    private Integer quantity;

    @Schema(description = "Supplier ID (required for purchases)", example = "123e4567-e89b-12d3-a456-426614174001")
    private String supplierId;

    private String description;

    private String note;


}
