package com.example.eLibrary.service;

import com.example.eLibrary.dto.AuthorDto;
import com.example.eLibrary.mapper.AuthorMapper;
import com.example.eLibrary.model.Author;
import com.example.eLibrary.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;
    private AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public Author save(AuthorDto authorDto) {
        Author author = authorMapper.map(authorDto);
        Author savedAuthor = authorRepository.save(author);
        return savedAuthor;
    }

    public List<AuthorDto> findAllAuthors() {
        return authorMapper.mapListToAuthorDto(authorRepository.findAll());
    }

    public Author findByFirstNameAndLastName(String firstName, String lastName) {
        return authorRepository.findByFirstNameAndLastNameIgnoreCase(firstName, lastName);
    }

    public List<Author> findAllById(List<Integer> ids) {
        return authorRepository.findAllById(ids);
    }

    public AuthorDto findById(Integer id) {
        Optional<Author> optionalAuthor = authorRepository.findById(id);
        if(!optionalAuthor.isPresent()) {
            throw new RuntimeException("There is no author with id " + id);
        }
        return authorMapper.map(optionalAuthor.get());
    }

    public void deleteAuthorById(Integer id) {
//        AuthorDto authorDto = findById(id);
//        Author author = authorMapper.map(authorDto);
//        if (author.getBooks().size() > 0) {
//            throw new RuntimeException("You cannot delete an author with existing books.");
//        }
        authorRepository.deleteById(id);
    }
}
