package com.biblioteca.svc_miembros.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MemberRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "El tipo de miembro es obligatorio")
    private String memberType;
}
