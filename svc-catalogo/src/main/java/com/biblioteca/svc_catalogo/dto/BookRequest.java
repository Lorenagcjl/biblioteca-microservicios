package com.biblioteca.svc_catalogo.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class BookRequest {
    @NotBlank(message = "El ISBN es obligatorio")
    private String isbn;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El autor es obligatorio")
    private String author;

    private String genre;

    @NotNull(message = "El año de publicación es obligatorio")
    private Integer publicationYear;

    @NotNull(message = "La disponibilidad es obligatoria")
    private Boolean available;

    @Min(0)
    private Integer totalCopies;

    @Min(0)
    private Integer availableCopies;
}
