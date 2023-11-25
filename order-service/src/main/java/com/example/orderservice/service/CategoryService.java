package com.example.orderservice.service;

import com.example.orderservice.entity.Category;

public interface CategoryService {
    Category findCategoryByName(String name);
}
