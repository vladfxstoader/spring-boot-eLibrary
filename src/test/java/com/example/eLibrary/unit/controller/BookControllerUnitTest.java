package com.example.eLibrary.unit.controller;

import com.example.eLibrary.controller.BookController;
import com.example.eLibrary.dto.AuthorDto;
import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.dto.CategoryDto;
import com.example.eLibrary.dto.PublisherDto;
import com.example.eLibrary.mapper.AuthorMapper;
import com.example.eLibrary.mapper.BookMapper;
import com.example.eLibrary.mapper.CategoryMapper;
import com.example.eLibrary.mapper.PublisherMapper;
import com.example.eLibrary.model.Book;
import com.example.eLibrary.model.Loan;
import com.example.eLibrary.model.Publisher;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@Slf4j
public class BookControllerUnitTest {

    @Mock
    private BookService bookService;

    @Mock
    private PublisherService publisherService;

    @Mock
    private CategoryService categoryService;

    @Mock
    private AuthorService authorService;

    @Mock
    private PublisherMapper publisherMapper;

    @Mock
    private AuthorMapper authorMapper;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private LoanService loanService;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testShowRegistrationForm() {
        // Arrange
        Authentication auth = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(auth);
        User user = new User();
        user.setStatus("ACCEPTED");
        when(userService.findByUsername("username")).thenReturn(user);

        when(publisherService.findAllPublishers()).thenReturn(new ArrayList<>());
        when(categoryService.findAllCategories()).thenReturn(new ArrayList<>());
        when(authorService.findAllAuthors()).thenReturn(new ArrayList<>());

        // Act
        String viewName = bookController.showRegistrationForm(model);

        // Assert
        assertEquals("add-book", viewName);
        verify(model).addAttribute(eq("userStatus"), anyString());
        verify(model).addAttribute(eq("book"), any(BookDto.class));
        verify(model).addAttribute(eq("publishers"), anyList());
        verify(model).addAttribute(eq("authors"), anyList());
        verify(model).addAttribute(eq("categories"), anyList());
        log.info("Show registration form test passed successfully");
    }
    @Test
    public void testRegistrationWithValidBook() {
        // Arrange
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Sample Title");
        bookDto.setYear(2021);
        bookDto.setStock(15);

        PublisherDto publisherDto = new PublisherDto(1, "Nemira");
        AuthorDto authorDto = new AuthorDto(1, "John", "Doe", null);
        List<AuthorDto> authors = new ArrayList<>();
        authors.add(authorDto);
        bookDto.setAuthors(authors);
        bookDto.setPublisher(publisherDto);
        List<CategoryDto> categories = Arrays.asList(
                new CategoryDto(1, "Fiction"),
                new CategoryDto(2, "Thriller")
        );
        bookDto.setCategories(categories);
        when(bookService.checkExistsBook(bookDto)).thenReturn(false);
        when(publisherService.findById(anyInt())).thenReturn(publisherDto);
        when(categoryService.findAllById(anyList())).thenReturn(new ArrayList<>());
        when(authorService.findAllById(anyList())).thenReturn(new ArrayList<>());

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String viewName = bookController.registration(bookDto, bindingResult, model);

        // Assert
        assertEquals("redirect:/add-book?success", viewName);
        verify(bookService).save(bookDto);
        log.info("Registration with valid book test passed successfully");
    }

    @Test
    public void testDeleteBook_WithLoans() {
        // Arrange
        int bookId = 1;
        Book book = new Book();
        List<Loan> loans = new ArrayList<>();
        loans.add(new Loan());
        book.setLoans(loans);
        when(bookService.findById(bookId)).thenReturn(book);

        // Act
        String viewName = bookController.deleteBook(bookId);

        // Assert
        assertEquals("redirect:/books?error", viewName);
        verify(bookService, never()).deleteBookById(anyInt());
        log.info("Delete book with loans test passed successfully");
    }

    @Test
    public void testSaveEditedBook_ValidBook() {
        // Arrange
        int bookId = 1;
        BookDto bookDto = new BookDto();
        bookDto.setTitle("New Book Title");

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        // Act
        String viewName = bookController.saveEditedBook(bookId, bookDto, result, model);

        // Assert
        assertEquals("redirect:/books", viewName);
        verify(bookService).save(bookDto);
        log.info("Save edited book for valid book test passed successfully");
    }

    @Test
    public void testSaveEditedBook_ExistingBook() {
        // Arrange
        int bookId = 1;
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Existing Book Title");

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(true);

        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("username");

        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(auth);

        User user = new User();
        user.setStatus("ACCEPTED");
        when(userService.findByUsername("username")).thenReturn(user);

        List<PublisherDto> publishers = new ArrayList<>();
        List<CategoryDto> categories = new ArrayList<>();
        List<AuthorDto> authors = new ArrayList<>();

        // Act
        String viewName = bookController.saveEditedBook(bookId, bookDto, result, model);

        // Assert
        assertEquals("edit-book", viewName);
        verify(model).addAttribute("userStatus", "accepted");
        verify(model).addAttribute("book", bookDto);
        verify(model).addAttribute("publishers", publishers);
        verify(model).addAttribute("authors", authors);
        verify(model).addAttribute("categories", categories);

        log.info("Save edited book for existing book test passed successfully");
    }

}
