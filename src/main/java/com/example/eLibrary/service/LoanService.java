package com.example.eLibrary.service;

import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.dto.LoanDto;
import com.example.eLibrary.mapper.LoanMapper;
import com.example.eLibrary.model.Book;
import com.example.eLibrary.model.Loan;
import com.example.eLibrary.repository.BookRepository;
import com.example.eLibrary.repository.LoanRepository;
import com.example.eLibrary.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LoanService {
    private LoanRepository loanRepository;
    private LoanMapper loanMapper;
    private UserRepository userRepository;
    private BookRepository bookRepository;

    public LoanService(LoanRepository loanRepository, LoanMapper loanMapper, UserRepository userRepository, BookRepository bookRepository) {
        this.loanRepository = loanRepository;
        this.loanMapper = loanMapper;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    public Loan save(LoanDto loanDto) {
        Loan loan = loanMapper.map(loanDto, userRepository, bookRepository);
        loan.setStatus("PENDING");
        Loan savedLoan = loanRepository.save(loan);
        return savedLoan;
    }

    public List<LoanDto> findAllLoans() {
        return loanMapper.mapListToLoanDto(loanRepository.findAll());
    }

    public Page<LoanDto> findAllLoans(Pageable pageable) {
        Page<Loan> loanPage = loanRepository.findAll(pageable);
        return loanPage.map(loanMapper::map);
    }

    public void changeLoanStatus(Integer id, String status) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if(!optionalLoan.isPresent()) {
            throw new RuntimeException("There is no loan with id " + id);
        }
        Loan loan = optionalLoan.get();
        loan.setStatus(status);
        if(Objects.equals(status, "ACCEPTED")) {
            Book book = loan.getBook();
            book.setStock(book.getStock() - 1);
            bookRepository.save(book);
        }
        if(Objects.equals(status, "RETURNED")) {
            Book book = loan.getBook();
            book.setStock(book.getStock() + 1);
            bookRepository.save(book);
        }
        loanRepository.save(loan);
    }

    public List<LoanDto> findAllLoansByUser(String username) {
        return loanMapper.mapListToLoanDto(loanRepository.findAllByUser_UsernameIgnoreCase(username));
    }

    public List<LoanDto> findAllLoansByUsernameAndStatusAndTitle(String title, String username, String status) {
        return loanMapper.mapListToLoanDto(loanRepository.findAllByBookTitleAndStatusAndUser_UsernameIgnoreCase(title, status, username));
    }

    public void changeActualReturnDate(Integer id, Date date) {
        Optional<Loan> optionalLoan = loanRepository.findById(id);
        if(!optionalLoan.isPresent()) {
            throw new RuntimeException("There is no loan with id " + id);
        }
        Loan loan = optionalLoan.get();
        loan.setActualReturnDate(date);
        loanRepository.save(loan);
    }

    public List<LoanDto> findAllLoansByStatusAndUsername(String username, String status) {
        return loanMapper.mapListToLoanDto(loanRepository.findAllByStatusAndUser_UsernameIgnoreCase(status, username));
    }
}
