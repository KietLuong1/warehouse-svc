package com.capstone.warehousesvc.services.impl;


import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.exceptions.NotFoundException;
import com.capstone.warehousesvc.models.Category;
import com.capstone.warehousesvc.models.Product;
import com.capstone.warehousesvc.models.Transaction;
import com.capstone.warehousesvc.repositories.CategoryRepository;
import com.capstone.warehousesvc.repositories.ProductRepository;
import com.capstone.warehousesvc.services.ProductService;
import com.capstone.warehousesvc.specification.ProductFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;


    //AFTER YOUR FRONTEND IS SETUP CHANGE THE IMAGE DIRECTORY TO THE FRONTEND YOU ARE USING
    private static final String IMAGE_DIRECTORY_2 = System.getProperty("user.dir") + "/frontend/public/products/";

    @Override
    public Response saveProduct(ProductDTO productDTO) {

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        //map our dto to product entity
        Product productToSave = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();

//        if (imageFile != null && !imageFile.isEmpty()) {
//            log.info("Image file exists");
//            String imagePath = saveImage2(imageFile); //use this when you have set up your frontend locally but haven't deployed to production
//
//            log.info("Image URL is: {}", imagePath);
//            productToSave.setImageUrl(imagePath);
//        }

        //save the product entity
        productRepository.save(productToSave);

        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();
    }

    @Override
    public Response updateProduct(ProductDTO productDTO) {

        //check if product exists
        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        //check if image is associated with the product to update and upload
//        if (imageFile != null && !imageFile.isEmpty()) {
//            String imagePath = saveImage2(imageFile); //use this when you have set up your frontend locally but haven't deployed to production
//
//            log.info("Image URL is: {}", imagePath);
//            existingProduct.setImageUrl(imagePath);
//        }

        //check if category is to be changed for the products
        if (productDTO.getCategoryId() != null && !productDTO.getCategoryId().isEmpty()) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category Not Found"));
            existingProduct.setCategory(category);
        }

        //check if product fields is to be changed and update
        if (productDTO.getName() != null && !productDTO.getName().isBlank()) {
            existingProduct.setName(productDTO.getName());
        }

        if (productDTO.getSku() != null && !productDTO.getSku().isBlank()) {
            existingProduct.setSku(productDTO.getSku());
        }

        if (productDTO.getDescription() != null && !productDTO.getDescription().isBlank()) {
            existingProduct.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
            existingProduct.setPrice(productDTO.getPrice());
        }

        if (productDTO.getStockQuantity() != null && productDTO.getStockQuantity() >= 0) {
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }
        //update the product
        productRepository.save(existingProduct);

        //Build our response
        return Response.builder()
                .status(200)
                .message("Product Updated successfully")
                .build();


    }

    @Override
    public Response getAllProducts(int page, int size, String keyword, String categoryId, String warehouseId) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));
        Specification<Product> spec = ProductFilter.byKeyword(keyword, categoryId, warehouseId);
        Page<Product> pageResult = productRepository.findAll(spec, pageable);

        List<ProductDTO> productDTOs = modelMapper.map(
                pageResult.getContent(),
                new TypeToken<List<ProductDTO>>() {}.getType()
        );

        return Response.builder()
                .status(200)
                .message("Success")
                .currentPage(page)
                .pageSize(size)
                .totalElements(pageResult.getTotalElements())
                .totalPages(pageResult.getTotalPages())
                .products(productDTOs)
                .build();
    }


    @Override
    public Response getProductById(String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        return Response.builder()
                .status(200)
                .message("success")
                .product(modelMapper.map(product, ProductDTO.class))
                .build();
    }

    @Override
    public Response deleteProduct(String id) {

        productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Product Deleted successfully")
                .build();
    }

    @Override
    public Response searchProduct(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Product> productPage = productRepository
                .findByNameContainingOrDescriptionContaining(keyword, keyword, pageable);

        if (productPage.isEmpty()) {
            throw new NotFoundException("Product Not Found");
        }

        List<ProductDTO> productDTOList = modelMapper.map(
                productPage.getContent(),
                new TypeToken<List<ProductDTO>>() {}.getType()
        );
        return Response.builder()
                .currentPage(page)
                .pageSize(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .status(200)
                .message("success")
                .products(productDTOList)
                .build();
    }

    //This saved image to the public folder in your frontend
    //Use this if your have setup your frontend
    private String saveImage2(MultipartFile imageFile) {
        //validate image and check if it is greater than 1GIB
        if (!Objects.requireNonNull(imageFile.getContentType()).startsWith("image/") || imageFile.getSize() > 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("Only image files under 1GIG is allowed");
        }

        //create the directory if it doesn't exist
        File directory = new File(IMAGE_DIRECTORY_2);

        if (!directory.exists()) {
            directory.mkdir();
            log.info("Directory was created");
        }
        //generate unique file name for the image
        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();

        //Get the absolute path of the image
        String imagePath = IMAGE_DIRECTORY_2 + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile); //we are writing the image to this folder
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving Image: " + e.getMessage());
        }
        return "products/" + uniqueFileName;


    }
}
