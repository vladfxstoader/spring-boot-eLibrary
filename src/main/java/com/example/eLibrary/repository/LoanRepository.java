package com.example.eLibrary.repository;

import com.example.eLibrary.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
    List<Loan> findAllByUser_UsernameIgnoreCase(String username);
    List<Loan> findAllByBookTitleAndStatusAndUser_UsernameIgnoreCase(String bookTitle, String status, String username);
    List<Loan> findAllByStatusAndUser_UsernameIgnoreCase(String status, String username);
}
