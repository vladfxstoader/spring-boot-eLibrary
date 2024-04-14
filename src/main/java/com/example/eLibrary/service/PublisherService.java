package com.example.eLibrary.service;

import com.example.eLibrary.dto.PublisherDto;
import com.example.eLibrary.mapper.PublisherMapper;
import com.example.eLibrary.model.Publisher;
import com.example.eLibrary.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherService {
    private PublisherRepository publisherRepository;
    private PublisherMapper publisherMapper;

    public PublisherService(PublisherRepository publisherRepository, PublisherMapper publisherMapper) {
        this.publisherRepository = publisherRepository;
        this.publisherMapper = publisherMapper;
    }

    public Publisher save(PublisherDto publisherDto) {
        Publisher publisher = publisherMapper.map(publisherDto);
        Publisher savedPublisher = publisherRepository.save(publisher);
        return savedPublisher;
    }

    public List<PublisherDto> findAllPublishers() {
        return publisherMapper.mapListToPublisherDto(publisherRepository.findAll());
    }

    public Publisher findByName(String name) {
        return publisherRepository.findByNameIgnoreCase(name);
    }

    public void deletePublisherById(Integer id) {
        publisherRepository.deleteById(id);
    }

    public PublisherDto findById(Integer id) {
        Optional<Publisher> optionalPublisher = publisherRepository.findById(id);
        if(!optionalPublisher.isPresent())
            throw new RuntimeException("There is no publisher with id " + id);
        return publisherMapper.map(optionalPublisher.get());
    }
}
