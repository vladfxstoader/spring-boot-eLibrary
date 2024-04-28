package com.example.eLibrary.unit.service;

import com.example.eLibrary.dto.LoanDto;
import com.example.eLibrary.mapper.LoanMapper;
import com.example.eLibrary.model.Book;
import com.example.eLibrary.model.Loan;
import com.example.eLibrary.model.User;
import com.example.eLibrary.repository.BookRepository;
import com.example.eLibrary.repository.LoanRepository;
import com.example.eLibrary.repository.UserRepository;
import com.example.eLibrary.service.LoanService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LoanServiceUnitTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private LoanService loanService;

    @Test
    void testSave() {
        // Arrange
        LoanDto loanDto = new LoanDto();
        loanDto.setId(1);
        loanDto.setUsername("test_user");
        loanDto.setBookId(1);
        loanDto.setNumberOfDays(7);

        User user = new User();
        user.setUsername("test_user");

        Book book = new Book();
        book.setId(1);
        book.setTitle("Test Book");
        book.setStock(5);

        Loan loan = new Loan();
        loan.setId(1);
        loan.setLoanDate(new Date());
        loan.setExpectedReturnDate(new Date());
        loan.setStatus("PENDING");
        loan.setUser(user);
        loan.setBook(book);

        when(loanMapper.map(any(LoanDto.class), any(UserRepository.class), any(BookRepository.class))).thenReturn(loan);
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        // Act
        Loan savedLoan = loanService.save(loanDto);

        // Assert
        assertNotNull(savedLoan);
        assertEquals(loan, savedLoan);
        verify(loanRepository, times(1)).save(any(Loan.class));

        log.info("Loan saved successfully");
    }

    @Test
    void testFindAllLoans() {
        Loan loan = new Loan();
        LoanDto loanDto = new LoanDto();

        when(loanRepository.findAll()).thenReturn(Collections.singletonList(loan));

        when(loanMapper.mapListToLoanDto(Collections.singletonList(loan))).thenReturn(Collections.singletonList(loanDto));

        List<LoanDto> foundLoans = loanService.findAllLoans();

        assertEquals(1, foundLoans.size());
        assertEquals(loanDto, foundLoans.get(0));

        log.info("All loans found successfully");
    }

    @Test
    void testChangeLoanStatusAccepted() {
        Integer loanId = 1;
        Loan loan = new Loan();
        loan.setId(loanId);
        Book book = new Book();
        book.setStock(5); // initial stock
        loan.setBook(book);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        loanService.changeLoanStatus(loanId, "ACCEPTED");

        assertEquals("ACCEPTED", loan.getStatus());

        // Verify that the book stock is decreased by 1
        assertEquals(4, book.getStock());

        verify(loanRepository, times(1)).save(loan);
        verify(bookRepository, times(1)).save(book);

        log.info("Change loan status to ACCEPTED passed successfully");
    }

    @Test
    void testChangeLoanStatusReturned() {
        Integer loanId = 1;
        Loan loan = new Loan();
        loan.setId(loanId);
        Book book = new Book();
        book.setStock(5); // initial stock
        loan.setBook(book);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        loanService.changeLoanStatus(loanId, "RETURNED");

        assertEquals("RETURNED", loan.getStatus());

        // Verify that the book stock is increased by 1
        assertEquals(6, book.getStock());

        verify(loanRepository, times(1)).save(loan);
        verify(bookRepository, times(1)).save(book);

        log.info("Change loan status to RETURNED passed successfully");
    }

    @Test
    void testChangeLoanStatusLoanNotFound() {
        Integer loanId = 1;

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        // Call the changeLoanStatus method with status "ACCEPTED"
        // Expecting a RuntimeException due to loan not found
        assertThrows(RuntimeException.class, () -> loanService.changeLoanStatus(loanId, "ACCEPTED"));

        verify(loanRepository, times(1)).findById(loanId);

        // Verify that bookRepository.save was not called
        verifyNoInteractions(bookRepository);
    }

    @Test
    void testChangeActualReturnDate() {
        Integer loanId = 1;
        Date date = new Date();
        Loan loan = new Loan();
        loan.setId(loanId);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        loanService.changeActualReturnDate(loanId, date);

        assertEquals(date, loan.getActualReturnDate());

        verify(loanRepository, times(1)).save(loan);

        log.info("Return date changed successfully");
    }

}
