package com.example.eLibrary.unit.service;

import com.example.eLibrary.dto.CategoryDto;
import com.example.eLibrary.mapper.CategoryMapper;
import com.example.eLibrary.model.Category;
import com.example.eLibrary.repository.CategoryRepository;
import com.example.eLibrary.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryServiceUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveCategory() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Fiction");

        Category savedCategory = new Category();
        savedCategory.setId(1);
        savedCategory.setName("Fiction");

        when(categoryMapper.map(categoryDto)).thenReturn(new Category());
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);

        // Act
        Category result = categoryService.save(categoryDto);

        // Assert
        assertEquals(savedCategory, result);
        verify(categoryMapper).map(categoryDto);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    public void testFindCategoryByName() {
        // Arrange
        String categoryName = "Fiction";
        Category category = new Category();
        category.setId(1);
        category.setName("Fiction");

        when(categoryRepository.findByNameIgnoreCase(categoryName)).thenReturn(category);

        // Act
        Category result = categoryService.findByName(categoryName);

        // Assert
        assertEquals(category, result);
        verify(categoryRepository).findByNameIgnoreCase(categoryName);
    }

    @Test
    public void testDeleteCategoryById() {
        // Arrange
        int categoryId = 1;

        // Act
        categoryService.deleteCategoryById(categoryId);

        // Assert
        verify(categoryRepository).deleteById(categoryId);
    }

}
