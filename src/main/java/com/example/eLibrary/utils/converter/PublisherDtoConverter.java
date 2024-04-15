package com.example.eLibrary.utils.converter;

import com.example.eLibrary.dto.PublisherDto;
import com.example.eLibrary.service.PublisherService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class PublisherDtoConverter implements Converter<String, PublisherDto> {

    private final PublisherService publisherService;

    public PublisherDtoConverter(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Override
    public PublisherDto convert(String id) {
        return publisherService.findById(Integer.parseInt(id));
    }
}