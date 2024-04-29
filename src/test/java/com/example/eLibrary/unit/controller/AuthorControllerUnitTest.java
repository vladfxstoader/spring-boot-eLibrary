package com.example.eLibrary.unit.controller;

import com.example.eLibrary.controller.AuthorController;
import com.example.eLibrary.dto.AuthorDto;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.AuthorService;
import com.example.eLibrary.service.BookService;
import com.example.eLibrary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
public class AuthorControllerUnitTest {

    @Mock
    private AuthorService authorService;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @InjectMocks
    private AuthorController authorController;

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

        // Act
        String viewName = authorController.showRegistrationForm(model);

        // Assert
        assertEquals("add-author", viewName);
        verify(model).addAttribute(eq("userStatus"), anyString());
        verify(model).addAttribute(eq("author"), any(AuthorDto.class));
        log.info("Show registration form test passed successfully");
    }

    @Test
    public void testRegistrationWithValidAuthor() {
        // Arrange
        AuthorDto authorDto = new AuthorDto();
        authorDto.setFirstName("John");
        authorDto.setLastName("Doe");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(authorService.findByFirstNameAndLastName(anyString(), anyString())).thenReturn(null);

        // Act
        String viewName = authorController.registration(authorDto, bindingResult, model);

        // Assert
        assertEquals("redirect:/add-author?success", viewName);
        verify(authorService).save(authorDto);
        log.info("Registration with valid author test passed successfully");
    }

    @Test
    public void testDeleteAuthorWithNoAssociatedBooks() {
        // Arrange
        int authorId = 1;
        AuthorDto authorDto = new AuthorDto();
        when(authorService.findById(authorId)).thenReturn(authorDto);
        when(bookService.findAllByAuthorName(anyString(), anyString())).thenReturn(new ArrayList<>());

        // Act
        String viewName = authorController.deleteAuthor(authorId);

        // Assert
        assertEquals("redirect:/authors", viewName);
        verify(authorService).deleteAuthorById(authorId);
        log.info("Delete author with no associated books test passed successfully");
    }

    @Test
    public void testShowEditAuthorForm() {
        // Arrange
        int authorId = 1;
        Authentication auth = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(auth);
        User user = new User();
        user.setStatus("ACCEPTED");
        when(userService.findByUsername("username")).thenReturn(user);
        AuthorDto authorDto = new AuthorDto();
        when(authorService.findById(authorId)).thenReturn(authorDto);

        // Act
        String viewName = authorController.showEditAuthorForm(authorId, model);

        // Assert
        assertEquals("edit-author", viewName);
        verify(model).addAttribute(eq("userStatus"), anyString());
        verify(model).addAttribute(eq("author"), any(AuthorDto.class));
        log.info("Show edit author form test passed successfully");
    }

}
