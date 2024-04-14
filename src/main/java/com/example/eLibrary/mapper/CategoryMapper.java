package com.example.eLibrary.mapper;

import com.example.eLibrary.dto.CategoryDto;
import com.example.eLibrary.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public Category map(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setName(categoryDto.getName());
        return category;
    }

    public CategoryDto map(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public List<Category> mapListToCategory(List<CategoryDto> categoryDtos) {
        return categoryDtos.stream().map(categoryDto -> map(categoryDto)).collect(Collectors.toList());
    }

    public List<CategoryDto> mapListToCategoryDto(List<Category> categories) {
        return categories.stream().map(category -> map(category)).collect(Collectors.toList());
    }
}
