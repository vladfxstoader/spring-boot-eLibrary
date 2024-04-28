package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Loan;
import com.example.eLibrary.repository.LoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class LoanRepositoryUnitTest {
    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private LoanRepositoryUnitTest loanRepositoryUnitTest;

    @Test
    void testFindAllByUser_UsernameIgnoreCase() {
        String username = "test_user";
        Loan loan = new Loan();

        when(loanRepository.findAllByUser_UsernameIgnoreCase(username)).thenReturn(Collections.singletonList(loan));
        List<Loan> loans = loanRepository.findAllByUser_UsernameIgnoreCase(username);

        assertEquals(1, loans.size());
        assertEquals(loan, loans.get(0));
    }

    @Test
    void testFindAllByBookTitleAndStatusAndUser_UsernameIgnoreCase() {
        String bookTitle = "Test Book";
        String status = "PENDING";
        String username = "test_user";
        Loan loan = new Loan();

        when(loanRepository.findAllByBookTitleAndStatusAndUser_UsernameIgnoreCase(bookTitle, status, username))
                .thenReturn(Collections.singletonList(loan));
        List <Loan> loans = loanRepository.findAllByBookTitleAndStatusAndUser_UsernameIgnoreCase(bookTitle, status, username);

        assertEquals(1, loans.size());
        assertEquals(loan, loans.get(0));
    }

    @Test
    void testFindAllByStatusAndUser_UsernameIgnoreCase() {
        String status = "PENDING";
        String username = "test_user";
        Loan loan = new Loan();

        when(loanRepository.findAllByStatusAndUser_UsernameIgnoreCase(status, username))
                .thenReturn(Collections.singletonList(loan));

        List<Loan> loans = loanRepository.findAllByStatusAndUser_UsernameIgnoreCase(status, username);

        assertEquals(1, loans.size());
        assertEquals(loan, loans.get(0));
    }
}
