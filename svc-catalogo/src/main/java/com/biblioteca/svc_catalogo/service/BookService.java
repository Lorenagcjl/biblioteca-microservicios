package com.biblioteca.svc_catalogo.service;

import com.biblioteca.svc_catalogo.dto.BookRequest;
import com.biblioteca.svc_catalogo.model.Book;
import com.biblioteca.svc_catalogo.repository.BookRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private ObjectMapper objectMapper;

    public Map<String, Object> saveBook(BookRequest dto) {
        Book book = new Book();
        book.setIsbn(dto.getIsbn());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setGenre(dto.getGenre());
        book.setPublicationYear(dto.getPublicationYear());
        book.setAvailable(dto.getAvailable());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getAvailableCopies());

        bookRepository.save(book);

        redis.delete("books:all");
        redis.delete("book:" + dto.getIsbn());

        return Map.of("mensaje", "Libro creado exitosamente", "isbn", dto.getIsbn());
    }

    public Map<String, Object> getBookByIsbn(String isbn) {
        String cacheKey = "book:" + isbn;
        String cached = redis.opsForValue().get(cacheKey);

        if (cached != null) {
            try {
                Map<String, Object> bookCache = objectMapper.readValue(
                        cached, new TypeReference<Map<String, Object>>() {});
                return Map.of("fuente", "CACHE REDIS (2ms)", "datos", bookCache);
            } catch (Exception e) {
                redis.delete(cacheKey);
            }
        }

        Optional<Book> bookOpt = bookRepository.findById(isbn);

        if (bookOpt.isEmpty()) {
            return Map.of("error", "Libro no encontrado: " + isbn);
        }

        Book book = bookOpt.get();

        try {
            String jsonBook = objectMapper.writeValueAsString(book);
            redis.opsForValue().set(cacheKey, jsonBook, 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("Error guardando en caché: " + e.getMessage());
        }

        return Map.of("fuente", "Base de Datos PostgreSQL", "datos", book);
    }
}