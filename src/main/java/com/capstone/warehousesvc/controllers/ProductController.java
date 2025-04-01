package com.capstone.warehousesvc.controllers;


import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Tag(name = "Products", description = "Product inventory management")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(
        summary = "Add product", 
        description = "Add a new product with image upload (Admin only)"
    )
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> saveProduct(
            @Parameter(description = "Product image file") @RequestParam("imageFile") MultipartFile imageFile,
            @Parameter(description = "Product name", required = true) @RequestParam("name") String name,
            @Parameter(description = "Product SKU (Stock Keeping Unit)", required = true) @RequestParam("sku") String sku,
            @Parameter(description = "Product price", required = true) @RequestParam("price") BigDecimal price,
            @Parameter(description = "Available stock quantity", required = true) @RequestParam("stockQuantity") Integer stockQuantity,
            @Parameter(description = "Category ID", required = true) @RequestParam("categoryId") Long categoryId,
            @Parameter(description = "Product description") @RequestParam(value = "description", required = false) String description
    ) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setSku(sku);
        productDTO.setPrice(price);
        productDTO.setStockQuantity(stockQuantity);
        productDTO.setCategoryId(categoryId);
        productDTO.setDescription(description);

        return ResponseEntity.ok(productService.saveProduct(productDTO, imageFile));
    }

    @Operation(summary = "Get all products")
    @GetMapping("/all")
    public ResponseEntity<Response> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Search products", description = "Search products by name or SKU")
    @GetMapping("/search")
    public ResponseEntity<Response> searchProduct(@Parameter(description = "Search term") @RequestParam String search) {
        return ResponseEntity.ok(productService.searchProduct(search));
    }

    @Operation(summary = "Update product", description = "Update an existing product (Admin only)")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(
            @Parameter(description = "Product image file") @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Parameter(description = "Product information") @RequestParam("productDTO") ProductDTO productDTO
    ) {
        return ResponseEntity.ok(productService.updateProduct(productDTO, imageFile));
    }

    @Operation(summary = "Delete product", description = "Delete a product (Admin only)")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
