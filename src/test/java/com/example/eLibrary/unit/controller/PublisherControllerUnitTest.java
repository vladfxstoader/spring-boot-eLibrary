package com.example.eLibrary.unit.controller;

import com.example.eLibrary.controller.PublisherController;
import com.example.eLibrary.dto.PublisherDto;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.BookService;
import com.example.eLibrary.service.PublisherService;
import com.example.eLibrary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
public class PublisherControllerUnitTest {

    @Mock
    private PublisherService publisherService;

    @Mock
    private BookService bookService;

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Model model;

    @InjectMocks
    private PublisherController publisherController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRegistrationWithValidPublisher() {
        // Arrange
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setName("Publisher");

        // Act
        String viewName = publisherController.registration(publisherDto, bindingResult, model);

        // Assert
        assertEquals("redirect:/add-publisher?success", viewName);
        verify(publisherService).save(publisherDto);
        log.info("Publisher registration test passed successfully");
    }

    @Test
    public void testDeletePublisherWithNoAssociatedBooks() {
        // Arrange
        int publisherId = 1;
        when(publisherService.findById(publisherId)).thenReturn(new PublisherDto());
        when(bookService.findAllByPublisherName(anyString())).thenReturn(new ArrayList<>());

        // Act
        String viewName = publisherController.deletePublisher(publisherId);

        // Assert
        assertEquals("redirect:/publishers", viewName);
        verify(publisherService).deletePublisherById(publisherId);
        log.info("Delete publisher with no associated books test passed successfully");
    }

    @Test
    public void testSaveEditedPublisherWithValidPublisher() {
        // Arrange
        int publisherId = 1;
        PublisherDto publisherDto = new PublisherDto();
        publisherDto.setId(publisherId);
        publisherDto.setName("Publisher");

        // Act
        String viewName = publisherController.saveEditedPublisher(publisherId, publisherDto);

        // Assert
        assertEquals("redirect:/publishers", viewName);
        verify(publisherService).save(publisherDto);
        log.info("Save edited publisher with valid publisher test passed successfully");
    }

    @Test
    public void testShowEditPublisherForm() {
        // Arrange
        int publisherId = 1;
        // Mock the authentication object
        Authentication auth = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        // Set up the security context with the authentication object
        SecurityContextHolder.setContext(securityContext);
        Mockito.when(securityContext.getAuthentication()).thenReturn(auth);

        // Mock the userService to return a user with a non-null status
        User user = new User();
        user.setStatus("ACCEPTED"); // Set a non-null status
        when(userService.findByUsername(anyString())).thenReturn(user);

        when(publisherService.findById(publisherId)).thenReturn(new PublisherDto());

        // Act
        String viewName = publisherController.showEditPublisherForm(publisherId, model);

        // Assert
        assertEquals("edit-publisher", viewName);
        verify(model).addAttribute(eq("userStatus"), anyString());
        verify(model).addAttribute(eq("publisher"), any(PublisherDto.class));
        log.info("Show edit publisher form test passed successfully");
    }
}
