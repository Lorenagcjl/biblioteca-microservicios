package com.biblioteca.svc_prestamos.controller;

import com.biblioteca.svc_prestamos.dto.LoanRequest;
import com.biblioteca.svc_prestamos.service.LoanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoanController {
    @Autowired
    private LoanService loanService;

    @PostMapping
    public Map<String, Object> createLoan(@Valid @RequestBody LoanRequest dto) {
        return loanService.createLoan(dto);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getLoan(@PathVariable String id) {
        return loanService.getLoanById(id);
    }

    @PutMapping("/{id}/devolver")
    public Map<String, Object> returnLoan(@PathVariable String id) {
        return loanService.returnLoan(id);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("servicio", "svc-prestamos", "estado", "OK");
    }
}
