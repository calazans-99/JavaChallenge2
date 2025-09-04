package com.grupo.dto;

import jakarta.validation.constraints.*;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SensorDTO implements Serializable {

    private Long idSensor;

    @NotNull(message = "Id da moto é obrigatório")
    private Long idMoto;

    // Temperatura pode ser nula dependendo do caso, mas valide se necessário
    @DecimalMin(value = "-100.0", message = "Temperatura mínima é -100.0")
    @DecimalMax(value = "200.0", message = "Temperatura máxima é 200.0")
    private Double temperatura;

    @NotNull(message = "Status 'ligada' é obrigatório")
    @Pattern(regexp = "^[SN]$", message = "Ligada deve ser 'S' ou 'N'")
    private String ligada; // 'S' ou 'N'
}
