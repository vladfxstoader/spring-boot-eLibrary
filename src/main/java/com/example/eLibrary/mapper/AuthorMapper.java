package com.example.eLibrary.mapper;

import com.example.eLibrary.dto.AuthorDto;
import com.example.eLibrary.model.Author;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthorMapper {
    private AuthorDetailsMapper authorDetailsMapper;

    public AuthorMapper(AuthorDetailsMapper authorDetailsMapper) {
        this.authorDetailsMapper = authorDetailsMapper;
    }

    public Author map(AuthorDto authorDto) {
        Author author = new Author();
        author.setId(authorDto.getId());
        author.setFirstName(authorDto.getFirstName());
        author.setLastName(authorDto.getLastName());
        author.setAuthorDetails(authorDetailsMapper.map(authorDto.getAuthorDetails()));
        return author;
    }

    public AuthorDto map(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        authorDto.setAuthorDetails(authorDetailsMapper.map(author.getAuthorDetails()));
        return authorDto;
    }

    public List<Author> mapListToAuthor(List<AuthorDto> authorDtos) {
        return authorDtos.stream().map(authorDto -> map(authorDto)).collect(Collectors.toList());
    }

    public List<AuthorDto> mapListToAuthorDto(List<Author> authors) {
        return authors.stream().map(author -> map(author)).collect(Collectors.toList());
    }
}
