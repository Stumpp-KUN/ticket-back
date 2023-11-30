package com.example.orderservice.service;

import com.example.orderservice.dto.CategoryDTO;
import com.example.orderservice.entity.Category;

public interface CategoryService {

    CategoryDTO getById(Long id);
    CategoryDTO getCategoryByName(String name);
}
