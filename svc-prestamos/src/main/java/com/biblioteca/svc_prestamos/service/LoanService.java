package com.biblioteca.svc_prestamos.service;

import com.biblioteca.svc_prestamos.dto.LoanRequest;
import com.biblioteca.svc_prestamos.model.Loan;
import com.biblioteca.svc_prestamos.repository.LoanRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> createLoan(LoanRequest dto) {
        Loan loan = new Loan();
        loan.setId("LOAN-" + UUID.randomUUID().toString());
        loan.setIsbn(dto.getIsbn());
        loan.setMemberId(dto.getMemberId());
        loan.setStatus("ACTIVO");
        loan.setLoanDate(new Date().toString());

        loanRepository.save(loan);

        return Map.of("mensaje", "Préstamo registrado exitosamente", "id", loan.getId());
    }

    public Map<String, Object> getLoanById(String id) {
        String cacheKey = "loan:" + id;
        String cached = redis.opsForValue().get(cacheKey);

        if (cached != null) {
            try {
                Map<String, Object> loanCache = objectMapper.readValue(
                        cached, new TypeReference<Map<String, Object>>() {});
                return Map.of("fuente", "CACHE REDIS (2ms)", "datos", loanCache);
            } catch (Exception e) {
                redis.delete(cacheKey);
            }
        }

        Optional<Loan> loanOpt = loanRepository.findById(id);

        if (loanOpt.isEmpty()){
            return Map.of("error", "Préstamo no encontrado: " + id);
        }

        Loan loan = loanOpt.get();

        try {
            String jsonLoan = objectMapper.writeValueAsString(loan);
            redis.opsForValue().set(cacheKey, jsonLoan, 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.err.println("Error en caché: " + e.getMessage());
        }

        return Map.of("fuente", "BASE DE DATOS (~80ms)", "datos", loan);
    }

    public Map<String, Object> returnLoan(String id) {
        Optional<Loan> loanOpt = loanRepository.findById(id);
        if (loanOpt.isEmpty()) return Map.of("error", "No encontrado");

        Loan loan = loanOpt.get();
        loan.setStatus("DEVUELTO");
        loan.setActualReturnDate(new Date().toString());
        loanRepository.save(loan);

        redis.delete("loan:" + id);
        return Map.of("mensaje", "Préstamo marcado como devuelto", "id", id);
    }
}