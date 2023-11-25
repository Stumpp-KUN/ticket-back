package com.example.orderservice.service.impl;

import com.example.orderservice.entity.Category;
import com.example.orderservice.repository.CategoryRepository;
import com.example.orderservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category findCategoryByName(String name) {
        log.info("Finding category by name {}",name);

        return categoryRepository.findCategoryByName(name).orElseThrow(()->new NoSuchElementException());
    }
}
