package com.example.eLibrary.unit.controller;

import com.example.eLibrary.controller.LoanController;
import com.example.eLibrary.dto.LoanDto;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.LoanService;
import com.example.eLibrary.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class LoanControllerUnitTest {

    @Mock
    private LoanService loanService;

    @Mock
    private UserService userService;

    @Mock
    private Model model;

    @InjectMocks
    private LoanController loanController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoansUserStatusAccepted() {
        // Arrange
        String username = "user1";
        Authentication auth = new UsernamePasswordAuthenticationToken(username, "password");
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(auth);
        User user = new User();
        user.setStatus("ACCEPTED");
        when(userService.findByUsername(username)).thenReturn(user);

        // Create a list of loans with at least one loan
        List<LoanDto> loans = new ArrayList<>();
        loans.add(new LoanDto()); // Add at least one loan

        Page<LoanDto> loanPage = mock(Page.class);
        when(loanService.findAllLoans(any())).thenReturn(loanPage);
        when(loanPage.getContent()).thenReturn(loans);
        when(model.addAttribute("loans", loans)).thenReturn(model);
        when(model.addAttribute("currentPage", 0)).thenReturn(model);
        when(model.addAttribute("pageSize", 10)).thenReturn(model);
        when(model.addAttribute("totalPages", loanPage.getTotalPages())).thenReturn(model);

        // Act
        String viewName = loanController.loans(model, 0, 10);

        // Assert
        assertEquals("loans", viewName);
        verify(model).addAttribute("userStatus", "accepted");
        verify(model).addAttribute("loans", loans);
        verify(model).addAttribute("currentPage", 0);
        verify(model).addAttribute("pageSize", 10);
        verify(model).addAttribute("totalPages", loanPage.getTotalPages());

    }

    @Test
    public void testAcceptLoan() {
        // Arrange
        int loanId = 1;

        // Act
        String viewName = loanController.acceptLoan(loanId);

        // Assert
        assertEquals("redirect:/loans", viewName);
        verify(loanService).changeLoanStatus(loanId, "ACCEPTED");
    }

    @Test
    public void testDeclineLoan() {
        // Arrange
        int loanId = 1;

        // Act
        String viewName = loanController.declineLoan(loanId);

        // Assert
        assertEquals("redirect:/loans", viewName);
        verify(loanService).changeLoanStatus(loanId, "DECLINED");
    }

    @Test
    public void testReturnLoan() {
        // Arrange
        int loanId = 1;

        // Act
        String viewName = loanController.returnLoan(loanId);

        // Assert
        assertEquals("redirect:/loans", viewName);
        verify(loanService).changeLoanStatus(loanId, "RETURNED");
        verify(loanService).changeActualReturnDate(eq(loanId), any(Date.class));
    }


}