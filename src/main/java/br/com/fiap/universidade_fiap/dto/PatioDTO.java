package br.com.fiap.universidade_fiap.dto;

import br.com.fiap.universidade_fiap.model.Patio;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatioDTO(
        Long id,
        @NotBlank @Size(max = 120) String nome,
        @NotNull @Min(1) Integer capacidade,
        @Size(max = 255) String localizacao
) {
    public static PatioDTO from(Patio p) {
        return new PatioDTO(
                p.getId(),
                p.getNome(),
                p.getCapacidade(),
                p.getLocalizacao()
        );
    }

    public Patio toEntity() {
        Patio p = new Patio();
        p.setId(id);
        p.setNome(nome);
        p.setCapacidade(capacidade);
        p.setLocalizacao(localizacao);
        return p;
    }
}
