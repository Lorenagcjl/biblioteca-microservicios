package com.biblioteca.svc_prestamos.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoanRequest {
    @NotBlank(message = "El ISBN es obligatorio")
    private String isbn;

    @NotBlank(message = "El ID del miembro es obligatorio")
    private String memberId;
}
