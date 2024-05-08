package com.example.eLibrary.unit.repository;

import com.example.eLibrary.model.Loan;
import com.example.eLibrary.repository.LoanRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("h2")
@Slf4j
class LoanRepositoryH2Test {

    @Autowired
    LoanRepository loanRepository;

    @Test
    void testFindAllByUser_UsernameIgnoreCase() {
        String username = "user2";

        List<Loan> loans = loanRepository.findAllByUser_UsernameIgnoreCase(username);

        assertNotNull(loans);

        log.info("Loans for user '{}':", username);
        loans.forEach(loan -> {
            log.info("Loan ID: {}", loan.getId());
            log.info("Book Title: {}", loan.getBook().getTitle());
            log.info("Loan Status: {}", loan.getStatus());
        });
    }

    @Test
    void testFindAllByBookTitleAndStatusAndUser_UsernameIgnoreCase() {
        String bookTitle = "Amintiri din copilarie";
        String status = "ACCEPTED";
        String username = "user2";

        List<Loan> loans = loanRepository.findAllByBookTitleAndStatusAndUser_UsernameIgnoreCase(bookTitle, status, username);

        assertNotNull(loans);

        log.info("Loans for book '{}' with status '{}' for user '{}':", bookTitle, status, username);
        loans.forEach(loan -> {
            log.info("Loan ID: {}", loan.getId());
            log.info("Loan Status: {}", loan.getStatus());
        });
    }

    @Test
    void testFindAllByStatusAndUser_UsernameIgnoreCase() {
        String status = "ACCEPTED";
        String username = "user2";

        List<Loan> loans = loanRepository.findAllByStatusAndUser_UsernameIgnoreCase(status, username);

        assertNotNull(loans);

        log.info("Loans for user '{}' with status '{}' for user '{}':", username, status);
        loans.forEach(loan -> {
            log.info("Loan ID: {}", loan.getId());
            log.info("Loan Status: {}", loan.getStatus());
        });
    }
}