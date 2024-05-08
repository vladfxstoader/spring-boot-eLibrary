package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Author;
import com.example.eLibrary.repository.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
class AuthorRepositoryH2Test {

    @Autowired
    AuthorRepository authorRepository;

    @Test
    void getAuthors() {
        List<Author> authors = authorRepository.findAll();
        assertNotNull(authors);

        log.info("getAuthors...");
        authors.forEach(author -> log.info(author.getLastName()));
    }

    @Test
    void getAuthor() {
        Author author = authorRepository.findByFirstNameAndLastNameIgnoreCase("Tudor", "Arghdezi");
        if (author != null) {
            assertNotNull(author);

            log.info("getAuthor...");
            log.info(String.valueOf(author.getId()));
            log.info(author.getFirstName());
            log.info(author.getLastName());
        }
        else
            log.info("Author not found with the specified first and last names.");
    }
}
