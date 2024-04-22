package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Category;
import com.example.eLibrary.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryRepositoryUnitTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void testFindByNameIgnoreCase() {
        // Create a category
        Category category = new Category();
        category.setId(1);
        category.setName("Fiction");

        // Mocking the repository behavior
        when(categoryRepository.findByNameIgnoreCase("Fiction")).thenReturn(category);

        // Call the findByNameIgnoreCase method
        Category foundCategory = categoryRepository.findByNameIgnoreCase("Fiction");

        // Assertion
        assertEquals(category, foundCategory);
    }

    @Test
    void testFindByNameIgnoreCase_CategoryNotFound() {
        // Mocking the repository behavior for category not found
        when(categoryRepository.findByNameIgnoreCase("NonExistentCategory")).thenReturn(null);

        // Call the findByNameIgnoreCase method for a category that does not exist
        Category foundCategory = categoryRepository.findByNameIgnoreCase("NonExistentCategory");

        // Assertion
        assertEquals(null, foundCategory);
    }
}