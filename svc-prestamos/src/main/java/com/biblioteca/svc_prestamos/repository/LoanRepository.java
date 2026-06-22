package com.biblioteca.svc_prestamos.repository;

import com.biblioteca.svc_prestamos.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, String> {
    List<Loan> findByMemberId(String memberId);

    List<Loan> findByStatus(String status);
}
