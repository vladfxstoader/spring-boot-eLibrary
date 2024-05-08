package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Author;
import com.example.eLibrary.model.Category;
import com.example.eLibrary.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
class CategoryRepositoryH2Test {

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    void getCategories() {
        List<Category> categories = categoryRepository.findAll();
        assertNotNull(categories);

        log.info("getCategories...");
        categories.forEach(category -> log.info(category.getName()));
    }

    @Test
    void testFindByNameIgnoreCase() {
        String categoryName = "Fiction";

        Category foundCategory = categoryRepository.findByNameIgnoreCase(categoryName);

        if (foundCategory != null) {
            log.info("Found category:");
            log.info("Category ID: {}", foundCategory.getId());
            log.info("Category Name: {}", foundCategory.getName());
        } else {
            log.info("No category found with the name '{}'.", categoryName);
        }
    }
}
