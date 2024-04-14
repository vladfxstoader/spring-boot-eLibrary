package com.example.eLibrary.mapper;

import com.example.eLibrary.dto.PublisherDto;
import com.example.eLibrary.model.Publisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PublisherMapper {
    public Publisher map(PublisherDto publisherDto) {
        Publisher publisher = new Publisher();
        publisher.setId(publisherDto.getId());
        publisher.setName(publisherDto.getName());
        return publisher;
    }
    public PublisherDto map(Publisher publisher) {
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setId(publisher.getId());
        publisherDto.setName(publisher.getName());
        return publisherDto;
    }

    public List<Publisher> mapListToPublisher(List<PublisherDto> publisherDtos) {
        return publisherDtos.stream().map(publisherDto -> map(publisherDto)).collect(Collectors.toList());
    }

    public List<PublisherDto> mapListToPublisherDto(List<Publisher> publishers) {
        return publishers.stream().map(publisher -> map(publisher)).collect(Collectors.toList());
    }
}
