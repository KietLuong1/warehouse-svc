package com.capstone.warehousesvc.services;

import com.capstone.warehousesvc.dtos.CategoryDTO;
import com.capstone.warehousesvc.dtos.Response;

import java.util.UUID;

public interface CategoryService {

    Response createCategory(CategoryDTO categoryDTO);

    Response getAllCategories();

    Response getCategoryById(UUID id);

    Response updateCategory(UUID id, CategoryDTO categoryDTO);

    Response deleteCategory(UUID id);
}
