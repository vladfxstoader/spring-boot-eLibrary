package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Author;
import com.example.eLibrary.repository.AuthorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorRepositoryUnitTest {

    @Mock
    private AuthorRepository authorRepository;

    @Test
    void testFindByFirstNameAndLastNameIgnoreCase() {
        // Create an author
        Author author = new Author();
        author.setId(1);
        author.setFirstName("John");
        author.setLastName("Doe");

        // Mocking the repository behavior
        when(authorRepository.findByFirstNameAndLastNameIgnoreCase("John", "Doe")).thenReturn(author);

        // Call the findByFirstNameAndLastNameIgnoreCase method
        Author foundAuthor = authorRepository.findByFirstNameAndLastNameIgnoreCase("John", "Doe");

        // Assertion
        assertEquals(author, foundAuthor);
    }

    @Test
    void testFindByFirstNameAndLastNameIgnoreCase_NotFound() {
        // Mocking the repository behavior for not found case
        when(authorRepository.findByFirstNameAndLastNameIgnoreCase("Unknown", "Author")).thenReturn(null);

        // Call the findByFirstNameAndLastNameIgnoreCase method for not found case
        Author foundAuthor = authorRepository.findByFirstNameAndLastNameIgnoreCase("Unknown", "Author");

        // Assertion
        assertEquals(null, foundAuthor);
    }
}
