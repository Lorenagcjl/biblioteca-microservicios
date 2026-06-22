package com.biblioteca.svc_catalogo.controller;

import com.biblioteca.svc_catalogo.dto.BookRequest;
import com.biblioteca.svc_catalogo.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/{isbn}")
    public Map<String, Object> getBook(@PathVariable String isbn) {
        return bookService.getBookByIsbn(isbn);
    }

    @PostMapping
    public Map<String, Object> createBook(@Valid @RequestBody BookRequest dto) {
        return bookService.saveBook(dto);
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("servicio", "svc-catalogo", "estado", "OK");
    }
}