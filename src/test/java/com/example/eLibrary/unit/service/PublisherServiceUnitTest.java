package com.example.eLibrary.unit.service;

import com.example.eLibrary.dto.PublisherDto;
import com.example.eLibrary.mapper.PublisherMapper;
import com.example.eLibrary.model.Publisher;
import com.example.eLibrary.repository.PublisherRepository;
import com.example.eLibrary.service.PublisherService;
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
class PublisherServiceUnitTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private PublisherMapper publisherMapper;

    @InjectMocks
    private PublisherService publisherService;

    @Test
    void testSave() {
        // Arrange
        PublisherDto publisherDto = new PublisherDto();
        Publisher publisher = new Publisher();
        when(publisherMapper.map(publisherDto)).thenReturn(publisher);
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        // Act
        Publisher savedPublisher = publisherService.save(publisherDto);

        // Assert
        assertNotNull(savedPublisher);
        verify(publisherRepository, times(1)).save(publisher);
        log.info("Publisher saved successfully");
    }

    @Test
    void testFindAllPublishers() {
        // Arrange
        List<Publisher> publishers = new ArrayList<>();
        when(publisherRepository.findAll()).thenReturn(publishers);
        when(publisherMapper.mapListToPublisherDto(publishers)).thenReturn(new ArrayList<>());

        // Act
        List<PublisherDto> foundPublishers = publisherService.findAllPublishers();

        // Assert
        assertNotNull(foundPublishers);
        assertEquals(0, foundPublishers.size());
        verify(publisherRepository, times(1)).findAll();
        log.info("All publishers retrieved successfully");
    }

    @Test
    void testFindByName() {
        // Arrange
        String publisherName = "Test Publisher";
        Publisher publisher = new Publisher();
        when(publisherRepository.findByNameIgnoreCase(publisherName)).thenReturn(publisher);

        // Act
        Publisher foundPublisher = publisherService.findByName(publisherName);

        // Assert
        assertNotNull(foundPublisher);
        assertEquals(publisher, foundPublisher);
        verify(publisherRepository, times(1)).findByNameIgnoreCase(publisherName);
        log.info("Publisher '{}' found successfully", publisherName);
    }

    @Test
    void testDeletePublisherById() {
        // Arrange
        int publisherId = 1;

        // Act
        publisherService.deletePublisherById(publisherId);

        // Assert
        verify(publisherRepository, times(1)).deleteById(publisherId);
        log.info("Publisher with ID '{}' deleted successfully", publisherId);
    }

    @Test
    void testFindById() {
        // Arrange
        int publisherId = 2;
        Publisher publisher = new Publisher();
        publisher.setId(publisherId);
        Optional<Publisher> optionalPublisher = Optional.of(publisher);
        when(publisherRepository.findById(publisherId)).thenReturn(optionalPublisher);

        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setId(publisherId);

        when(publisherMapper.map(publisher)).thenReturn(publisherDto);

        // Act
        PublisherDto foundPublisher = publisherService.findById(publisherId);

        // Assert
        assertNotNull(foundPublisher);
        assertEquals(publisherId, foundPublisher.getId().intValue());

        verify(publisherRepository, times(1)).findById(publisherId);
        log.info("Publisher with ID '{}' found successfully", publisherId);
    }
}
