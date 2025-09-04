package com.grupo.dto;

import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;

    @NotBlank(message = "Tipo de usuário é obrigatório")
    @Pattern(regexp = "^(Administrador|Operador)$",
            message = "Tipo de usuário deve ser 'Administrador' ou 'Operador'")
    private String tipoUsuario;
}
