package com.example.eLibrary.unit.controller;

import com.example.eLibrary.controller.CategoryController;
import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.dto.CategoryDto;
import com.example.eLibrary.model.Category;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.BookService;
import com.example.eLibrary.service.CategoryService;
import com.example.eLibrary.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryControllerUnitTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegistrationWithValidCategory() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Fiction");

        // Act
        String viewName = categoryController.registration(categoryDto, bindingResult, model);

        // Assert
        assertEquals("redirect:/add-category?success", viewName);
        verify(categoryService).save(categoryDto);
    }

    @Test
    public void testDeleteCategoryWithNoAssociatedBooks() {
        // Arrange
        int categoryId = 1;
        when(categoryService.findById(categoryId)).thenReturn(new CategoryDto());
        when(bookService.findAllByCategoryName(anyString())).thenReturn(new ArrayList<>());

        // Act
        String viewName = categoryController.deleteCategory(categoryId);

        // Assert
        assertEquals("redirect:/categories", viewName);
        verify(categoryService).deleteCategoryById(categoryId);
    }

    @Test
    public void testSaveEditedCategoryWithValidCategory() {
        // Arrange
        int categoryId = 1;
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        categoryDto.setName("Fiction");

        // Act
        String viewName = categoryController.saveEditedCategory(categoryId, categoryDto, bindingResult, model);

        // Assert
        assertEquals("redirect:/categories", viewName);
        verify(categoryService).save(categoryDto);
    }
}
