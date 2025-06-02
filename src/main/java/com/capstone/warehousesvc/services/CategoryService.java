package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.CategoryDTO;
import com.capstone.warehousesvc.dtos.Response;
import com.capstone.warehousesvc.dtos.response.PagedResponse;

public interface CategoryService {

    Response createCategory(CategoryDTO categoryDTO);

    PagedResponse<CategoryDTO> getAllCategories(int page, int size);

    Response getCategoryById(String id);

    Response updateCategory(String id, CategoryDTO categoryDTO);

    Response deleteCategory(String id);
}
