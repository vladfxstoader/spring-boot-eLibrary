package com.example.eLibrary.controller;

import com.example.eLibrary.dto.BookDto;
import com.example.eLibrary.dto.LoanDto;
import com.example.eLibrary.model.User;
import com.example.eLibrary.service.BookService;
import com.example.eLibrary.service.LoanService;
import com.example.eLibrary.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
public class LoanController {
    private LoanService loanService;
    private BookService bookService;
    private UserService userService;

    public LoanController(LoanService loanService, BookService bookService, UserService userService) {
        this.loanService = loanService;
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/add-loan")
    public String showRegistrationForm(Model model){
        LoanDto loan = new LoanDto();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }

        List<LoanDto> previousLoans = loanService.findAllLoansByUser(name);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();

        calendar.setTime(currentDate);
        Integer problem = 0;

        for (LoanDto prevLoan : previousLoans) {
            Date expirationDate = prevLoan.getExpectedReturnDate();
            calendar.setTime(expirationDate);
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            Date sevenDaysAfterExpiration = calendar.getTime();
            calendar.setTime(expirationDate);
            calendar.add(Calendar.DAY_OF_YEAR, 30);
            Date thirtyDaysAfterExpiration = calendar.getTime();
            String status = prevLoan.getStatus();
            if(Objects.equals(status, "ACCEPTED") && currentDate.after(thirtyDaysAfterExpiration)) {
                problem = 2;
                Date problemDate = prevLoan.getExpectedReturnDate();
                calendar.setTime(problemDate);
                calendar.add(Calendar.DAY_OF_YEAR, 1080);
                Date thirtysixMonthAfter = calendar.getTime();
                if(currentDate.after(thirtysixMonthAfter)) {
                    problem = 0;
                }
            }
            if(Objects.equals(status, "RETURNED")) {
                if(prevLoan.getActualReturnDate().after(thirtyDaysAfterExpiration)) {
                    //  a adus-o dupa 30 zile de la expirare
                    problem = 2; // trebuie sa treaca 36 luni de la expirare
                    Date problemDate = prevLoan.getActualReturnDate();
                    calendar.setTime(problemDate);
                    calendar.add(Calendar.DAY_OF_YEAR, 1080);
                    Date thirtysixMonthAfter = calendar.getTime();
                    if(currentDate.after(thirtysixMonthAfter)) {
                        problem = 0;
                    }
                }
                else if(prevLoan.getActualReturnDate().after(sevenDaysAfterExpiration)) {
                    Date problemDate = prevLoan.getActualReturnDate();
                    problem = 1; // a adus cartea cu intarziere 7 zile si daca nu au trecut 6 luni de atunci, nu poate face imprumut
                    calendar.setTime(problemDate);
                    calendar.add(Calendar.DAY_OF_YEAR, 180);
                    Date sixMonthAfter = calendar.getTime();
                    if(currentDate.after(sixMonthAfter)) {
                        problem = 0;
                    }
                }
            }
        }

        List<BookDto> books = bookService.findAllBooksWithPositiveStock();
        model.addAttribute("loan", loan);
        model.addAttribute("books", books);
        model.addAttribute("problem", problem);
        return "add-loan";
    }

    @PostMapping("/add-loan/save")
    public String registration(@Valid @ModelAttribute("loan") LoanDto loanDto,
                               BindingResult result,
                               Model model){
        Integer bookId = loanDto.getBookId();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        List<LoanDto> allLoans = loanService.findAllLoansByUser(name);

        Calendar calendar = Calendar.getInstance();
        Date currentDate = new Date();

        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date thirtyDaysAgo = calendar.getTime();

        Integer numberOfLoansDeclinedLast30Days = 0;
        Integer numberOfCurrentLoansForBook = 0;

        for (LoanDto loan : allLoans) {
            Integer id = loan.getBookId();
            String status = loan.getStatus();
            if (status.equals("DECLINED") && loan.getLoanDate().after(thirtyDaysAgo) && id.equals(bookId)) {
                numberOfLoansDeclinedLast30Days += 1;
            }
            if(status.equals("ACCEPTED") && id.equals(bookId)) {
                numberOfCurrentLoansForBook += 1;
            }
        }

        if(numberOfLoansDeclinedLast30Days > 0) {
            //result.rejectValue("book", null, "You cannot loan a book within 30 days of last rejection.");
            model.addAttribute("loan", loanDto);
            return "redirect:/add-loan?errorRejection";
        }


        if(numberOfCurrentLoansForBook > 0) {
            //result.rejectValue("book", null, "You cannot loan the same book if you don't return it first.");
            model.addAttribute("loan", loanDto);
            return "redirect:/add-loan?errorBookNotReturned";
        }

        if(result.hasErrors()){
            model.addAttribute("loan", loanDto);
            return "redirect:/add-loan?error";
        }

        loanDto.setLoanDate(currentDate);

        calendar.setTime(currentDate);
        Integer days = loanDto.getNumberOfDays();

        if (days < 1) {
            model.addAttribute("loan", loanDto);
            return "redirect:/add-loan?error";
        }

        calendar.add(Calendar.DAY_OF_YEAR, days);
        Date newDate = calendar.getTime();
        loanDto.setExpectedReturnDate(newDate);


        loanDto.setUsername(name);

        loanService.save(loanDto);
        return "redirect:/add-loan?success";
    }

    @GetMapping("/loans")
    public String loans(Model model,
                        @RequestParam(defaultValue = "0") Integer page,
                        @RequestParam(defaultValue = "10") Integer size){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }

        Page<LoanDto> loanPage = loanService.findAllLoans(PageRequest.of(page, size));

        List<LoanDto> loans = loanPage.getContent();

        if(loans.size() == 0) {
            return "loans?noData";
        }

        model.addAttribute("loans", loans);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", loanPage.getTotalPages());
        return "loans";
    }

    @PostMapping("/accept-loan/{id}")
    public String acceptLoan(@PathVariable("id") Integer id) {
        loanService.changeLoanStatus(id, "ACCEPTED");
        return "redirect:/loans";
    }

    @PostMapping("/decline-loan/{id}")
    public String declineLoan(@PathVariable("id") Integer id) {
        loanService.changeLoanStatus(id, "DECLINED");
        return "redirect:/loans";
    }

    @GetMapping("/loans-user")
    public String loansUser(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        if(!user.getStatus().equals("ACCEPTED")) {
            model.addAttribute("userStatus", "declined");
        }
        else {
            model.addAttribute("userStatus", "accepted");
        }
        List<LoanDto> loans = loanService.findAllLoansByUser(name);
        if(loans.size() == 0) {
            model.addAttribute("flag", "noLoans");
        }
        else {
            model.addAttribute("flag", "hasLoans");
        }
        model.addAttribute("loans", loans);
        return "loans-user.html";
    }

    @GetMapping("/loans-user-exp-date")
    public String loansUserDays(@RequestParam Integer numberOfDays, Model model) {
        if(numberOfDays < 1) {
            return "redirect:/loans-user?error";
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Date currentDate = new Date();
        List<LoanDto> loans = loanService.findAllLoansByUser(name);
        List<LoanDto> loansToReturn = new ArrayList<>();
        for (LoanDto loanDto : loans) {
            if (Objects.equals(loanDto.getStatus(), "ACCEPTED")) {
                Date expirationDate = loanDto.getExpectedReturnDate();
                long diffInMillies = expirationDate.getTime() - currentDate.getTime();
                long daysLeft = TimeUnit.MILLISECONDS.toDays(diffInMillies);
                if(daysLeft < numberOfDays && daysLeft >= 0) {
                    loansToReturn.add(loanDto);
                }
            }
        }
        if(loansToReturn.size() == 0) {
            model.addAttribute("flag", "noLoans");
        }
        else {
            model.addAttribute("flag", "hasLoans");
        }
        model.addAttribute("loans", loansToReturn);
        model.addAttribute("numberOfDays", numberOfDays);
        return "loans-user-expiration-date.html";
    }

    @PostMapping("/return-loan/{id}")
    public String returnLoan(@PathVariable("id") Integer id) {
        loanService.changeLoanStatus(id, "RETURNED");
        Date currentDate = new Date();
        loanService.changeActualReturnDate(id, currentDate);
        return "redirect:/loans";
    }
}
