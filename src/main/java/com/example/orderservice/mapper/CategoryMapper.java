package com.example.orderservice.mapper;

import com.example.orderservice.dto.CategoryDTO;
import com.example.orderservice.entity.Category;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CategoryMapper {
    Category toEntity(CategoryDTO categoryDTO);
    CategoryDTO fromEntity(Category category);
}
