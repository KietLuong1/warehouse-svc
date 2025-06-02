package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.CategoryDTO;
import com.capstone.warehousesvc.dtos.Response;

public interface CategoryService {

    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(String id);

    Response updateCategory(String id, CategoryDTO categoryDTO);

    Response deleteCategory(String id);
}
