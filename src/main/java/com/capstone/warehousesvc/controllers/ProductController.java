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
@RequestMapping("/api/v1/products")
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
            @Parameter(description = "Category ID", required = true) @RequestParam("categoryId") String categoryId,
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
    public ResponseEntity<?> getAllProducts(
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Search products", description = "Search products by name or SKU")
    @GetMapping("/search")
    public ResponseEntity<?> searchProduct(
            @Parameter(description = "Search term") @RequestParam String search,
            @Parameter(description = "Page number (zero-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.searchProduct(search, page, size));
    }

    @Operation(summary = "Update product", description = "Update an existing product (Admin only)")
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateProduct(
            @Parameter(description = "Product image file") @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @Parameter(description = "Product ID", required = true) @RequestParam("productId") String productId,
            @Parameter(description = "Product name") @RequestParam(value = "name", required = false) String name,
            @Parameter(description = "Product SKU") @RequestParam(value = "sku", required = false) String sku,
            @Parameter(description = "Product price") @RequestParam(value = "price", required = false) BigDecimal price,
            @Parameter(description = "Available stock quantity") @RequestParam(value = "stockQuantity", required = false) Integer stockQuantity,
            @Parameter(description = "Category ID") @RequestParam(value = "categoryId", required = false) String categoryId,
            @Parameter(description = "Product description") @RequestParam(value = "description", required = false) String description
    ) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(productId);
        if (name != null) productDTO.setName(name);
        if (sku != null) productDTO.setSku(sku);
        if (price != null) productDTO.setPrice(price);
        if (stockQuantity != null) productDTO.setStockQuantity(stockQuantity);
        if (categoryId != null) productDTO.setCategoryId(categoryId);
        if (description != null) productDTO.setDescription(description);

        return ResponseEntity.ok(productService.updateProduct(productDTO, imageFile));
    }

    @Operation(summary = "Delete product", description = "Delete a product (Admin only)")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
