package com.example.eLibrary.utils.converter;

import com.example.eLibrary.dto.CategoryDto;
import com.example.eLibrary.service.CategoryService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CategoryDtoConverter implements Converter<String, CategoryDto> {
    private final CategoryService categoryService;
    public CategoryDtoConverter(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDto convert(String id) {
        return categoryService.findById(Integer.parseInt(id));
    }
}