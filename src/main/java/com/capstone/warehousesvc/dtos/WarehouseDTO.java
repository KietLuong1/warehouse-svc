package com.capstone.warehousesvc.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDTO {
    
    private Long id;
    
    @Schema(description = "Warehouse name", example = "Main Distribution Center")
    @NotBlank(message = "Name is required")
    private String name;
    
    @Schema(description = "Warehouse location address", example = "123 Storage St, Warehouse District")
    @NotBlank(message = "Location is required")
    private String location;
    
    @Schema(description = "Warehouse capacity in square meters", example = "5000")
    @NotNull(message = "Capacity is required")
    private Double capacity;
    
    @Schema(description = "Active status of warehouse", example = "true")
    private Boolean active = true;
} 