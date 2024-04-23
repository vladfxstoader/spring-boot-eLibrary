package com.example.eLibrary.unit.service;

import com.example.eLibrary.dto.AuthorDto;
import com.example.eLibrary.mapper.AuthorMapper;
import com.example.eLibrary.model.Author;
import com.example.eLibrary.repository.AuthorRepository;
import com.example.eLibrary.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AuthorServiceUnitTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void testSave() {
        // Arrange
        AuthorDto authorDto = new AuthorDto();
        Author author = new Author();
        when(authorMapper.map(authorDto)).thenReturn(author);
        when(authorRepository.save(author)).thenReturn(author);

        // Act
        Author savedAuthor = authorService.save(authorDto);

        // Assert
        assertNotNull(savedAuthor);
        verify(authorRepository, times(1)).save(author);
        log.info("Author saved successfully");
    }

    @Test
    void testFindAllAuthors() {
        // Arrange
        List<Author> authors = new ArrayList<>();
        when(authorRepository.findAll()).thenReturn(authors);
        when(authorMapper.mapListToAuthorDto(authors)).thenReturn(new ArrayList<>());

        // Act
        List<AuthorDto> foundAuthors = authorService.findAllAuthors();

        // Assert
        assertNotNull(foundAuthors);
        assertEquals(0, foundAuthors.size());
        verify(authorRepository, times(1)).findAll();
        log.info("All authors retrieved successfully");
    }

    @Test
    void testFindByFirstNameAndLastName() {
        // Arrange
        String firstName = "John";
        String lastName = "Doe";
        Author author = new Author();
        when(authorRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName)).thenReturn(author);

        // Act
        Author foundAuthor = authorService.findByFirstNameAndLastName(firstName, lastName);

        // Assert
        assertNotNull(foundAuthor);
        assertEquals(author, foundAuthor);
        verify(authorRepository, times(1)).findByFirstNameAndLastNameIgnoreCase(firstName, lastName);
        log.info("Author found successfully: {} {}", firstName, lastName);
    }

    @Test
    void testDeleteAuthorById() {
        // Arrange
        int authorId = 1;

        // Act
        authorService.deleteAuthorById(authorId);

        // Assert
        verify(authorRepository, times(1)).deleteById(authorId);
        log.info("Author with ID '{}' deleted successfully", authorId);
    }

    @Test
    void testFindById() {
        // Arrange
        int authorId = 1;
        Author author = new Author();
        author.setId(authorId);
        Optional<Author> optionalAuthor = Optional.of(author);
        when(authorRepository.findById(authorId)).thenReturn(optionalAuthor);

        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(authorId);

        when(authorMapper.map(author)).thenReturn(authorDto);

        // Act
        AuthorDto foundAuthor = authorService.findById(authorId);

        // Assert
        assertNotNull(foundAuthor);
        assertEquals(authorId, foundAuthor.getId().intValue());

        verify(authorRepository, times(1)).findById(authorId);
        log.info("Author with ID '{}' found successfully", authorId);
    }
}
