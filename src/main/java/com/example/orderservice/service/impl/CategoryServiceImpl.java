package com.example.orderservice.service.impl;

import com.example.orderservice.dto.CategoryDTO;
import com.example.orderservice.mapper.CategoryMapper;
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

    public final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO getById(Long id) {
        return categoryMapper.fromEntity(
                categoryRepository.findById(id).orElseThrow(()-> new NoSuchElementException("")));
    }

    @Override
    public CategoryDTO getCategoryByName(String name) {
        return categoryMapper.fromEntity(
                categoryRepository.findCategoryByName(name).orElseThrow(()->new NoSuchElementException("")));
    }

}
