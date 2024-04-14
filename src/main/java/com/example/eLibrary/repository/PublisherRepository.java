package com.example.eLibrary.repository;

import com.example.eLibrary.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {
    Publisher findByNameIgnoreCase(String name);
}
