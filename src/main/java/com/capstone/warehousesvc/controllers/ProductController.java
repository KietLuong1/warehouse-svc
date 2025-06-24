package com.capstone.warehousesvc.controllers;


import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Slf4j
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
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> saveProduct(@RequestBody ProductDTO productDTO
    ) {
        System.out.println("Product: " + productDTO);

        return ResponseEntity.ok(productService.saveProduct(productDTO));
    }


    @Operation(summary = "Get all products")
    @GetMapping("/all")
    public ResponseEntity<Response> getAllProducts(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.getAllProducts(page, size));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Search products", description = "Search products by name or SKU")
    @GetMapping("/search")
    public ResponseEntity<Response> searchProduct(@Parameter(description = "Search term") @RequestParam("keyword") String keyword,
                                                  @Parameter(description = "Page (zero-based)") @RequestParam(defaultValue = "1") int page,
                                                  @Parameter(description = "Size") @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.searchProduct(keyword, page, size));
    }

    @Operation(summary = "Update product", description = "Update an existing product (Admin only)")
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateProduct(
            @RequestBody ProductDTO productDTO,
            @PathVariable String id
    ) {
        productDTO.setProductId(id);

        return ResponseEntity.ok(productService.updateProduct(productDTO));
    }

    @Operation(summary = "Delete product", description = "Delete a product (Admin only)")
    @DeleteMapping("/delete/{id}")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> deleteProduct(@PathVariable String id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }
}
