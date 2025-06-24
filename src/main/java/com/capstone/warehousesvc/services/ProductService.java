package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.ProductDTO;
import com.capstone.warehousesvc.dtos.Response;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    Response saveProduct(ProductDTO productDTO);

    Response updateProduct(ProductDTO productDTO);

    Response getAllProducts(int page, int size);

    Response getProductById(String id);

    Response deleteProduct(String id);

    Response searchProduct(String keyword, int page, int size);
}
