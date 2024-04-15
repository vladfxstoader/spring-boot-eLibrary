package com.example.eLibrary.utils.converter;

import com.example.eLibrary.dto.AuthorDto;
import com.example.eLibrary.service.AuthorService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AuthorDtoConverter implements Converter<String, AuthorDto> {
    private final AuthorService authorService;

    public AuthorDtoConverter(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    public AuthorDto convert(String id) {
        return authorService.findById(Integer.parseInt(id));
    }
}
