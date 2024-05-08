package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Book;
import com.example.eLibrary.repository.BookRepository;
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
class BookRepositoryH2Test {

    @Autowired
    BookRepository bookRepository;

    @Test
    void testFindByTitleIgnoreCase() {
        String title = "Amintiri din copilarie";
        Book foundBook = bookRepository.findByTitleIgnoreCase(title);

        if (foundBook != null) {
            assertNotNull(foundBook);

            log.info("ID: {}", foundBook.getId());
            log.info("Title: {}", foundBook.getTitle());
        }
        else{
            log.info("Book not found with the specified title.");
        }
    }
    @Test
    void testFindByTitleAndPublisher_NameIgnoreCase() {
        String title = "Amintiri din copilarie";
        String publisherName = "Paralela 45";

        Book foundBook = bookRepository.findByTitleAndPublisher_NameIgnoreCase(title, publisherName);

        if (foundBook != null) {
            assertNotNull(foundBook);

            log.info("Found book:");
            log.info("ID: {}", foundBook.getId());
            log.info("Title: {}", foundBook.getTitle());
        } else {
            log.info("Book not found with the specified title and publisher name.");
        }
    }

    @Test
    void testFindAllByCategories_NameIgnoreCase() {
        String categoryName = "Fiction";

        List<Book> books = bookRepository.findAllByCategories_NameIgnoreCase(categoryName);

        if (books != null && !books.isEmpty()) {
            log.info("Found {} books for category '{}':", books.size(), categoryName);

            for (Book book : books) {
                log.info("Book ID: {}", book.getId());
                log.info("Title: {}", book.getTitle());
            }
        } else {
            log.info("No books found for category '{}'.", categoryName);
        }
    }
}
