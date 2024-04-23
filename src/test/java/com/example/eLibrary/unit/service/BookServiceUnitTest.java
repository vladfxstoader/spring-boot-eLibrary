package com.example.eLibrary.unit.service;

import com.example.eLibrary.dto.AuthorDto;
import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.dto.PublisherDto;
import com.example.eLibrary.mapper.BookMapper;
import com.example.eLibrary.model.Book;
import com.example.eLibrary.repository.BookRepository;
import com.example.eLibrary.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class BookServiceUnitTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @Test
    void testSave() {
        // Arrange
        BookDto bookDto = new BookDto();
        Book book = new Book();
        when(bookMapper.map(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        // Act
        Book savedBook = bookService.save(bookDto);

        // Assert
        assertNotNull(savedBook);
        verify(bookRepository, times(1)).save(book);
        log.info("Book saved successfully");
    }

    @Test
    void testCheckExistsBook() {
        // Arrange
        AuthorDto authorDto = new AuthorDto(1, "John", "Doe", null);
        PublisherDto publisherDto = new PublisherDto(1, "Nemira");
        BookDto matchingBookDto = new BookDto(1, "Sample Title", 2021, 15, publisherDto, List.of(authorDto), null);
        BookDto nonMatchingBookDto = new BookDto(2, "Different Title", 2022, 20, publisherDto, List.of(authorDto), null);

        List<BookDto> allBooks = Arrays.asList(matchingBookDto, new BookDto());
        when(bookService.findAllBooks()).thenReturn(allBooks);

        // Act
        boolean existsMatchingBook = bookService.checkExistsBook(matchingBookDto);
        boolean existsNonMatchingBook = bookService.checkExistsBook(nonMatchingBookDto);

        // Assert
        assertFalse(existsMatchingBook);
        assertTrue(existsNonMatchingBook);
        log.info("Checked if book exists successfully");
    }

    @Test
    void testFindAllBooks() {
        // Arrange
        List<Book> books = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);
        List<BookDto> expectedBookDtos = Arrays.asList(new BookDto(), new BookDto());
        when(bookMapper.mapListToBookDto(books)).thenReturn(expectedBookDtos);

        // Act
        List<BookDto> foundBooks = bookService.findAllBooks();

        // Assert
        assertNotNull(foundBooks);
        assertEquals(expectedBookDtos.size(), foundBooks.size());
        verify(bookRepository, times(1)).findAll();
        log.info("All books retrieved successfully");
    }

    @Test
    void testDeleteBookById() {
        // Arrange
        int bookId = 1;

        // Act
        bookService.deleteBookById(bookId);

        // Assert
        verify(bookRepository, times(1)).deleteById(bookId);
        log.info("Book with ID '{}' deleted successfully", bookId);
    }

}
