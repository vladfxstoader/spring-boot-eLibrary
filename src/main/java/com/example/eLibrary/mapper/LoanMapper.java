package com.example.eLibrary.mapper;

import com.example.eLibrary.dto.LoanDto;
import com.example.eLibrary.model.Book;
import com.example.eLibrary.model.Loan;
import com.example.eLibrary.model.User;
import com.example.eLibrary.repository.BookRepository;
import com.example.eLibrary.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LoanMapper {
    private BookMapper bookMapper;

    public LoanMapper(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    public Loan map(LoanDto loanDto, UserRepository userRepository, BookRepository bookRepository) {
        Loan loan = new Loan();
        loan.setId(loanDto.getId());
        loan.setLoanDate(loanDto.getLoanDate());
        loan.setActualReturnDate(loanDto.getActualReturnDate());
        loan.setExpectedReturnDate(loanDto.getExpectedReturnDate());
        User user = userRepository.findByUsername(loanDto.getUsername());
        if(user == null) {
            throw new RuntimeException("There is no user with username " + loanDto.getUsername());
        }
        loan.setUser(user);
        Optional<Book> book = bookRepository.findById(loanDto.getBookId());
        loan.setBook(book.get());
        loan.setStatus(loanDto.getStatus());
        loan.setObservations(loanDto.getObservations());
        return loan;
    }

    public LoanDto map(Loan loan) {
        LoanDto loanDto = new LoanDto();
        loanDto.setId(loan.getId());
        loanDto.setLoanDate(loan.getLoanDate());
        loanDto.setActualReturnDate(loan.getActualReturnDate());
        loanDto.setExpectedReturnDate(loan.getExpectedReturnDate());
        loanDto.setUsername(loan.getUser().getUsername());
        loanDto.setBookId(loan.getBook().getId());
        loanDto.setBookTitle(loan.getBook().getTitle());
        loanDto.setStatus(loan.getStatus());
        loanDto.setObservations(loan.getObservations());
        return loanDto;
    }

    public List<Loan> mapListToLoan(List<LoanDto> loanDtos, UserRepository userRepository, BookRepository bookRepository) {
        return loanDtos.stream().map(loanDto -> map(loanDto, userRepository, bookRepository)).collect(Collectors.toList());
    }

    public List<LoanDto> mapListToLoanDto(List<Loan> loans) {
        return loans.stream().map(loan -> map(loan)).collect(Collectors.toList());
    }
}
