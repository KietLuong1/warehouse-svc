package com.capstone.warehousesvc.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Product category details")
public class CategoryDTO {

    @Schema(description = "Category ID", example = "1")
    private Long id;

    @Schema(description = "Category name", example = "Electronics")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Products in this category")
    private List<ProductDTO> products;
}
