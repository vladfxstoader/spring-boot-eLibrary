package com.example.eLibrary.repository;

import com.example.eLibrary.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Author findByLastNameIgnoreCase(String lastName);
    Author findByFirstNameAndLastNameIgnoreCase(String firstName, String lastName);
}
