package com.example.eLibrary.service;

import com.example.eLibrary.dto.CategoryDto;
import com.example.eLibrary.mapper.CategoryMapper;
import com.example.eLibrary.model.Category;
import com.example.eLibrary.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public Category save(CategoryDto categoryDto) {
        Category category = categoryMapper.map(categoryDto);
        Category savedCategory = categoryRepository.save(category);
        return savedCategory;
    }

    public List<CategoryDto> findAllCategories() {
        return categoryMapper.mapListToCategoryDto(categoryRepository.findAll());
    }

    public Category findByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name);
    }

    public void deleteCategoryById(Integer id) {
//        CategoryDto categoryDto = findById(id);
//        Category category = categoryMapper.map(categoryDto);
//        if (category.getBooks().size() > 0) {
//            throw new RuntimeException("You cannot delete a category with existing books.");
//        }
        categoryRepository.deleteById(id);
    }

    public List<Category> findAllById(List<Integer> ids) {
        return categoryRepository.findAllById(ids);
    }

    public CategoryDto findById(Integer id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if(!optionalCategory.isPresent()) {
            throw new RuntimeException("There is no category with id " + id);
        }
        return categoryMapper.map(optionalCategory.get());
    }
}
