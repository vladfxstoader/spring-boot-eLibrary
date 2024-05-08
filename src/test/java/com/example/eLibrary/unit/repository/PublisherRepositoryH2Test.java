package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Publisher;
import com.example.eLibrary.repository.PublisherRepository;
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
class PublisherRepositoryH2Test {

    @Autowired
    PublisherRepository publisherRepository;

    @Test
    void getPublishers() {
        List<Publisher> publishers = publisherRepository.findAll();
        assertNotNull(publishers);

        log.info("getPublishers...");
        publishers.forEach(publisher -> log.info(publisher.getName()));
    }

    @Test
    void testFindByNameIgnoreCase() {
        String publisherName = "Nemira";

        Publisher foundPublisher = publisherRepository.findByNameIgnoreCase(publisherName);

        if (foundPublisher != null) {
            log.info("Found publisher:");
            log.info("Publisher ID: {}", foundPublisher.getId());
            log.info("Publisher Name: {}", foundPublisher.getName());
        } else {
            log.info("No publisher found with the name '{}'.", publisherName);
        }
    }
}
