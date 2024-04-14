package com.example.eLibrary.repository;

import com.example.eLibrary.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Category findByNameIgnoreCase(String name);
}
