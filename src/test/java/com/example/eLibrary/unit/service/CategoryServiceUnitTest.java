package com.example.eLibrary.unit.service;

import com.example.eLibrary.dto.CategoryDto;
import com.example.eLibrary.mapper.CategoryMapper;
import com.example.eLibrary.model.Category;
import com.example.eLibrary.repository.CategoryRepository;
import com.example.eLibrary.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CategoryServiceUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testSave() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto();
        Category category = new Category();
        when(categoryMapper.map(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        // Act
        Category savedCategory = categoryService.save(categoryDto);

        // Assert
        assertNotNull(savedCategory);
        verify(categoryRepository, times(1)).save(category);
        log.info("Category saved successfully");
    }

    @Test
    void testFindAllCategories() {
        // Arrange
        List<Category> categories = new ArrayList<>();
        when(categoryRepository.findAll()).thenReturn(categories);
        when(categoryMapper.mapListToCategoryDto(categories)).thenReturn(new ArrayList<>());

        // Act
        List<CategoryDto> foundCategories = categoryService.findAllCategories();

        // Assert
        assertNotNull(foundCategories);
        assertEquals(0, foundCategories.size());
        verify(categoryRepository, times(1)).findAll();
        log.info("All categories retrieved successfully");
    }

    @Test
    void testFindByName() {
        // Arrange
        String categoryName = "Test Category";
        Category category = new Category();
        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(category);

        // Act
        Category foundCategory = categoryService.findByName(categoryName);

        // Assert
        assertNotNull(foundCategory);
        assertEquals(category, foundCategory);
        verify(categoryRepository, times(1)).findByNameIgnoreCase(categoryName);
        log.info("Category '{}' found successfully", categoryName);
    }

    @Test
    void testDeleteCategoryById() {
        // Arrange
        int categoryId = 1;

        // Act
        categoryService.deleteCategoryById(categoryId);

        // Assert
        verify(categoryRepository, times(1)).deleteById(categoryId);
        log.info("Category with ID '{}' deleted successfully", categoryId);
    }
    @Test
    void findById() {
        int categoryId = 2;
        Category category = new Category();
        category.setId(categoryId);
        Optional<Category> optionalCategory = Optional.of(category);
        when(categoryRepository.findById(categoryId)).thenReturn(optionalCategory);

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);

        when(categoryMapper.map(category)).thenReturn(categoryDto);

        // Act
        CategoryDto foundCategory = categoryService.findById(categoryId);

        // Assert
        assertNotNull(foundCategory);
        assertEquals(categoryId, foundCategory.getId().intValue());

        verify(categoryRepository, times(1)).findById(categoryId);
        log.info("Category with ID '{}' found successfully", categoryId);
    }
}
