package com.example.eLibrary.service;

import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.mapper.BookMapper;
import com.example.eLibrary.model.Book;
import com.example.eLibrary.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private BookRepository bookRepository;
    private BookMapper bookMapper;

    public BookService(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    public Book save(BookDto bookDto) {
        Book book = bookMapper.map(bookDto);
        Book savedBook = bookRepository.save(book);
        return savedBook;
    }

    public Boolean checkExistsBook(BookDto bookDto) {
        List<BookDto> allBooks = findAllBooks();
        Integer number = 0;
        for(BookDto book: allBooks) {
            number = 0;
            if(bookDto.getTitle().equals(book.getTitle())) {
                number += 1;
            }
            if(bookDto.getYear().equals(book.getYear())) {
                number += 1;
            }
            if(bookDto.getAuthors().equals(book.getAuthors())) {
                number += 1;
            }
            if(bookDto.getPublisher().equals(book.getPublisher())) {
                number += 1;
            }
            if(number == 4) {
                return false;
            }
        }
        return true;
    }

    public List<BookDto> findAllBooks() {
        return bookMapper.mapListToBookDto(bookRepository.findAll());
    }

    public List<BookDto> findAllBooksWithPositiveStock() {
        return bookMapper.mapListToBookDto(bookRepository.findAllByStockGreaterThan(0));
    }

    public Book findById(Integer id) {
        Optional<Book> book  = bookRepository.findById(id);
        if(!book.isPresent()) {
            throw new RuntimeException("There is no book with id " + id);
        }
        return book.get();
    }

    public Book findByTitle(String title) {
        return bookRepository.findByTitleIgnoreCase(title);
    }
    public Book findByTitleAndPublisherName(String title, String name) {
        return bookRepository.findByTitleAndPublisher_NameIgnoreCase(title, name);
    }

    public Book findByTitleAndPublisherNameAndAuthorName(String title, String name, String firstName, String lastName) {
        return bookRepository.findByTitleAndPublisher_NameAndAuthors_FirstNameAndAuthors_LastNameIgnoreCase(title, name, firstName, lastName);
    }

    public void deleteBookById(Integer id) {
//        Optional<Book> optionalBook = bookRepository.findById(id);
//        if(optionalBook.get().getLoans().size() > 0) {
//            throw new RuntimeException("You cannot delete a book with loan history");
//        }
        bookRepository.deleteById(id);
    }

    public List<BookDto> findAllBooksByTitle(String title) {
        return bookMapper.mapListToBookDto(bookRepository.findAllBooksByTitleIgnoreCase(title.toUpperCase()));
    }

    public List<BookDto> findAllByCategoryName(String name) {
        return bookMapper.mapListToBookDto(bookRepository.findAllByCategories_NameIgnoreCase(name));
    }

    public List<BookDto> findAllByYear(Integer year) {
        return bookMapper.mapListToBookDto(bookRepository.findAllByYear(year));
    }

    public List<BookDto> findAllByAuthorName(String firstName, String lastName) {
        return bookMapper.mapListToBookDto(bookRepository.findAllByAuthors_FirstNameAndAuthors_LastNameIgnoreCase(firstName, lastName));
    }

    public List<BookDto> findAllByPublisherName(String name) {
        return bookMapper.mapListToBookDto(bookRepository.findAllByPublisher_NameIgnoreCase(name));
    }
}
