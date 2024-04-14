package com.example.eLibrary.mapper;

import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.model.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookMapper {
    private PublisherMapper publisherMapper;
    private AuthorMapper authorMapper;
    private CategoryMapper categoryMapper;

    public BookMapper(PublisherMapper publisherMapper, AuthorMapper authorMapper, CategoryMapper categoryMapper) {
        this.publisherMapper = publisherMapper;
        this.authorMapper = authorMapper;
        this.categoryMapper = categoryMapper;
    }
    public Book map(BookDto bookDto) {
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setYear(bookDto.getYear());
        book.setStock(bookDto.getStock());
        book.setPublisher(publisherMapper.map(bookDto.getPublisher()));
        book.setCategories(categoryMapper.mapListToCategory(bookDto.getCategories()));
        book.setAuthors(authorMapper.mapListToAuthor(bookDto.getAuthors()));
        return book;
    }

    public BookDto map(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setYear(book.getYear());
        bookDto.setStock(book.getStock());
        bookDto.setPublisher(publisherMapper.map(book.getPublisher()));
        bookDto.setCategories(categoryMapper.mapListToCategoryDto(book.getCategories()));
        bookDto.setAuthors(authorMapper.mapListToAuthorDto(book.getAuthors()));
        return bookDto;
    }

    public List<Book> mapListToBook(List<BookDto> bookDtos) {
        return bookDtos.stream().map(bookDto -> map(bookDto)).collect(Collectors.toList());
    }

    public List<BookDto> mapListToBookDto(List<Book> books) {
        return books.stream().map(book -> map(book)).collect(Collectors.toList());
    }
}
