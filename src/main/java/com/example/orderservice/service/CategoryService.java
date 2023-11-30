package com.example.orderservice.service;

import com.example.orderservice.dto.CategoryDTO;

public interface CategoryService {

    CategoryDTO getById(Long id);
    CategoryDTO getCategoryByName(String name);
}
