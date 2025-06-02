package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.response.PagedResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Response saveProduct(ProductDTO productDTO, MultipartFile imageFile);

    Response updateProduct(ProductDTO productDTO, MultipartFile imageFile);

    PagedResponse<ProductDTO> getAllProducts(int page, int size);

    Response getProductById(String id);

    Response deleteProduct(String id);

    PagedResponse<ProductDTO> searchProduct(String input, int page, int size);
}
