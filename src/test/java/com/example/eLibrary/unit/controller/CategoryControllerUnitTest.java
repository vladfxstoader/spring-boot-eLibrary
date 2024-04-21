package com.example.eLibrary.unit.controller;
import com.example.eLibrary.controller.CategoryController;
import com.example.eLibrary.dto.CategoryDto;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.BookService;
import com.example.eLibrary.service.CategoryService;
import com.example.eLibrary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
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
        log.info("Category registration test passed successfully");
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
        log.info("Delete category with no associated books test passed successfully");
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
        log.info("Save edited category with valid category test passed successfully");
    }

    @Test
    public void testShowEditCategoryForm() {
        // Arrange
        int categoryId = 1;
        // Mock the authentication object
        Authentication auth = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        // Set up the security context with the authentication object
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);

        // Mock the userService to return a user with a non-null status
        User user = new User();
        user.setStatus("ACCEPTED"); // Set a non-null status
        when(userService.findByUsername(anyString())).thenReturn(user);

        when(categoryService.findById(categoryId)).thenReturn(new CategoryDto());

        // Act
        String viewName = categoryController.showEditCategoryForm(categoryId, model);

        // Assert
        assertEquals("edit-category", viewName);
        verify(model).addAttribute(eq("userStatus"), anyString());
        verify(model).addAttribute(eq("category"), any(CategoryDto.class));
        log.info("Show edit category form test passed successfully");
    }
}
