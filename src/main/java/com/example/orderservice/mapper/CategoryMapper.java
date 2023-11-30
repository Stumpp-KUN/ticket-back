package com.example.orderservice.mapper;

import com.example.orderservice.dto.CategoryDTO;
import com.example.orderservice.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public abstract class CategoryMapper {
    public abstract Category toEntity(CategoryDTO categoryDTO);
    public abstract CategoryDTO fromEntity(Category category);
}
