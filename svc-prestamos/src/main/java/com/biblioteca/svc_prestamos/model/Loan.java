package com.biblioteca.svc_prestamos.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "loans")
public class Loan {
    @Id
    private String id;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "loan_date")
    private String loanDate;

    @Column(name = "estimated_return_date")
    private String estimatedReturnDate;

    @Column(name = "actual_return_date")
    private String actualReturnDate;

    private String status;
}
