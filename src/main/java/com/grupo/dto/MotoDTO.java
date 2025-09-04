package com.grupo.dto;

import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MotoDTO implements Serializable {

    private Long idMoto;

    @NotBlank(message = "Placa é obrigatória")
    @Size(min = 7, max = 8, message = "Placa deve ter entre 7 e 8 caracteres")
    // Opcional: regex simplificada (retire se não quiser restringir)
    // @Pattern(regexp = "^[A-Z]{3}[0-9][A-Z0-9][0-9]{2}$", message = "Placa em formato inválido")
    private String placa;

    @NotBlank(message = "Modelo é obrigatório")
    @Size(max = 50, message = "Modelo deve ter no máximo 50 caracteres")
    private String modelo;

    @NotNull(message = "Id do pátio é obrigatório")
    private Long idPatio;

    @NotNull(message = "Id do status é obrigatório")
    private Long idStatusMoto;

    @NotNull(message = "Posição X é obrigatória")
    private Integer posX;

    @NotNull(message = "Posição Y é obrigatória")
    private Integer posY;
}
