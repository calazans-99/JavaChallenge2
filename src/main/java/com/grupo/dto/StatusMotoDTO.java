package com.grupo.dto;

import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusMotoDTO implements Serializable {

    private Long idStatus;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 50, message = "Descrição deve ter no máximo 50 caracteres")
    private String descricao;
}
