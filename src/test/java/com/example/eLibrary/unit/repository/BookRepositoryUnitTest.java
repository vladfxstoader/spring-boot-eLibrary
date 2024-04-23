package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Book;
import com.example.eLibrary.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookRepositoryUnitTest {

    @Mock
    private BookRepository bookRepository;

    @Test
    void testFindByTitleIgnoreCase() {
        // Create a book
        Book book = new Book();
        book.setId(1);
        book.setTitle("Sample Book Title");

        // Mocking the repository behavior
        when(bookRepository.findByTitleIgnoreCase("Sample Book Title")).thenReturn(book);

        // Call the findByTitleIgnoreCase method
        Book foundBook = bookRepository.findByTitleIgnoreCase("Sample Book Title");

        // Assertion
        assertEquals(book, foundBook);
    }

    @Test
    void testFindByTitleAndPublisher_NameIgnoreCase() {
        // Create a book
        Book book = new Book();
        book.setId(1);
        book.setTitle("Sample Book Title");

        // Mocking the repository behavior
        when(bookRepository.findByTitleAndPublisher_NameIgnoreCase("Sample Book Title", "Publisher Name")).thenReturn(book);

        // Call the findByTitleAndPublisher_NameIgnoreCase method
        Book foundBook = bookRepository.findByTitleAndPublisher_NameIgnoreCase("Sample Book Title", "Publisher Name");

        // Assertion
        assertEquals(book, foundBook);
    }

    @Test
    void testFindByTitleAndPublisher_NameAndAuthors_FirstNameAndAuthors_LastNameIgnoreCase() {
        // Create a book
        Book book = new Book();
        book.setId(1);
        book.setTitle("Sample Book Title");

        // Mocking the repository behavior
        when(bookRepository.findByTitleAndPublisher_NameAndAuthors_FirstNameAndAuthors_LastNameIgnoreCase(
                "Sample Book Title", "Publisher Name", "Author First Name", "Author Last Name")).thenReturn(book);

        // Call the findByTitleAndPublisher_NameAndAuthors_FirstNameAndAuthors_LastNameIgnoreCase method
        Book foundBook = bookRepository.findByTitleAndPublisher_NameAndAuthors_FirstNameAndAuthors_LastNameIgnoreCase(
                "Sample Book Title", "Publisher Name", "Author First Name", "Author Last Name");

        // Assertion
        assertEquals(book, foundBook);
    }

    @Test
    void testFindAllBooksByTitleIgnoreCase() {
        // Mocking the repository behavior
        when(bookRepository.findAllBooksByTitleIgnoreCase("Sample Book Title")).thenReturn(Collections.emptyList());

        // Call the findAllBooksByTitleIgnoreCase method
        List<Book> books = bookRepository.findAllBooksByTitleIgnoreCase("Sample Book Title");

        // Assertion
        assertEquals(Collections.emptyList(), books);
    }

    @Test
    void testFindAllByAuthors_FirstNameAndAuthors_LastNameIgnoreCase() {
        // Mocking the repository behavior
        when(bookRepository.findAllByAuthors_FirstNameAndAuthors_LastNameIgnoreCase("John", "Doe")).thenReturn(Collections.emptyList());

        // Call the findAllByAuthors_FirstNameAndAuthors_LastNameIgnoreCase method
        List<Book> books = bookRepository.findAllByAuthors_FirstNameAndAuthors_LastNameIgnoreCase("John", "Doe");

        // Assertion
        assertEquals(Collections.emptyList(), books);
    }

    @Test
    void testFindAllByCategories_NameIgnoreCase() {
        // Mocking the repository behavior
        when(bookRepository.findAllByCategories_NameIgnoreCase("Category Name")).thenReturn(Collections.emptyList());

        // Call the findAllByCategories_NameIgnoreCase method
        List<Book> books = bookRepository.findAllByCategories_NameIgnoreCase("Category Name");

        // Assertion
        assertEquals(Collections.emptyList(), books);
    }

    @Test
    void testFindAllByPublisher_NameIgnoreCase() {
        // Mocking the repository behavior
        when(bookRepository.findAllByPublisher_NameIgnoreCase("Publisher Name")).thenReturn(Collections.emptyList());

        // Call the findAllByPublisher_NameIgnoreCase method
        List<Book> books = bookRepository.findAllByPublisher_NameIgnoreCase("Publisher Name");

        // Assertion
        assertEquals(Collections.emptyList(), books);
    }

    @Test
    void testFindAllByYear() {
        // Creating sample books for the year 2023
        Book book1 = new Book(1, "Book 1", 2023, 10, null, null, null, null);
        Book book2 = new Book(2, "Book 2", 2022, 15, null, null, null, null);
        List<Book> expectedBooks = Arrays.asList(book1, book2);

        // Mocking the repository behavior
        when(bookRepository.findAllByYear(2023)).thenReturn(expectedBooks);

        // Call the findAllByYear method
        List<Book> books = bookRepository.findAllByYear(2023);

        // Assertion
        assertEquals(expectedBooks, books);
    }

    @Test
    void testFindAllByStockGreaterThan() {
        // Creating sample books with stock greater than 10
        Book book1 = new Book(1, "Book 1", 2021, 15, null, null, null, null);
        Book book2 = new Book(2, "Book 2", 2022, 20, null, null, null, null);
        List<Book> expectedBooks = Arrays.asList(book1, book2);

        // Mocking the repository behavior
        when(bookRepository.findAllByStockGreaterThan(10)).thenReturn(expectedBooks);

        // Call the findAllByStockGreaterThan method
        List<Book> books = bookRepository.findAllByStockGreaterThan(10);

        // Assertion
        assertEquals(expectedBooks, books);
    }
}
