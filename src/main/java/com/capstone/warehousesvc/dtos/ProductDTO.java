package com.capstone.warehousesvc.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@Schema(description = "Product details")
public class ProductDTO {

    @Schema(description = "Product ID", example = "123e4567-e89b-12d3-a456-426614174000")
    private String id;

    @Schema(description = "Category ID this product belongs to", example = "123e4567-e89b-12d3-a456-426614174001")
    private String categoryId;

    @Schema(description = "Used for product requests", example = "123e4567-e89b-12d3-a456-426614174002")
    private String productId;

    @Schema(description = "Supplier ID for this product", example = "123e4567-e89b-12d3-a456-426614174003")
    private String supplierId;

    @Schema(description = "Product name", example = "Smartphone X12")
    private String name;

    @Schema(description = "Product SKU (Stock Keeping Unit)", example = "SMX12-BLACK-128")
    private String sku;

    @Schema(description = "Product price", example = "999.99")
    private BigDecimal price;

    @Schema(description = "Available stock quantity", example = "42")
    private Integer stockQuantity;

    @Schema(description = "Product description", example = "Latest model with enhanced camera and battery life")
    private String description;
    
    @Schema(description = "Product expiry date (for perishable items)")
    private LocalDateTime expiryDate;
    
    @Schema(description = "Product image URL", example = "/images/products/smartphone-x12.jpg")
    private String imageUrl;

    @Schema(description = "Product creation date")
    private LocalDateTime createdAt;

    @Schema(description = "Batch/Lot number", example = "LOT-2024-03-15-A")
    private String batchNumber;

    @Schema(description = "Warehouse ID where product is stored", example = "123e4567-e89b-12d3-a456-426614174004")
    private String warehouseId;

    @Schema(description = "Storage bin location within warehouse", example = "A12-B05-C03")
    private String binLocation;
}
