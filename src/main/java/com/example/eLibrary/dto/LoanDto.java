package com.example.eLibrary.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class LoanDto {
    private Integer id;
    private Date loanDate;
    private Date expectedReturnDate;
    private Date actualReturnDate;
    private String bookTitle;
    private Integer bookId;
    private String username;
    private String status;
    private String observations;
    private Integer numberOfDays;
}
