package com.example.orderservice.service;

import com.example.orderservice.entity.Category;

public interface CategoryService {

    Category getById(Long id);
    Category getCategoryByName(String name);
}
