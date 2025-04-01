package com.capstone.warehousesvc.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Supplier details")
public class SupplierDTO {

    @Schema(description = "Supplier ID", example = "1")
    private Long id;

    @Schema(description = "Supplier name", example = "ABC Distributors")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Supplier contact information (phone, email)", example = "+1 123-456-7890")
    @NotBlank(message = " contactInfo is required")
    private String contactInfo;

    @Schema(description = "Supplier address", example = "123 Supplier Street, City, Country")
    private String address;
}
