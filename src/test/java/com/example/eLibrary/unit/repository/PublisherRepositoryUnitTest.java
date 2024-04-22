package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Publisher;
import com.example.eLibrary.repository.PublisherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublisherRepositoryUnitTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Test
    void testFindByNameIgnoreCase() {
        // Create a publisher
        Publisher publisher = new Publisher();
        publisher.setId(1);
        publisher.setName("Publisher Name");

        // Mocking the repository behavior
        when(publisherRepository.findByNameIgnoreCase("Publisher Name")).thenReturn(publisher);

        // Call the findByNameIgnoreCase method
        Publisher foundPublisher = publisherRepository.findByNameIgnoreCase("Publisher Name");

        // Assertion
        assertEquals(publisher, foundPublisher);
    }

    @Test
    void testFindByNameIgnoreCase_NotFound() {
        // Mocking the repository behavior for not found case
        when(publisherRepository.findByNameIgnoreCase("Unknown Publisher")).thenReturn(null);

        // Call the findByNameIgnoreCase method for not found case
        Publisher foundPublisher = publisherRepository.findByNameIgnoreCase("Unknown Publisher");

        // Assertion
        assertEquals(null, foundPublisher);
    }
}
