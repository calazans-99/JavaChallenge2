package br.com.fiap.universidade_fiap.dto;

import br.com.fiap.universidade_fiap.model.Patio;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PatioDTO(
        Long id,

        @NotBlank
        @Size(max = 120)
        String nome,

        @NotNull
        @Min(1)
        Integer capacidade,

        @Size(max = 255)
        String localizacao,

        @NotNull @Min(1)
        Integer largura,

        @NotNull @Min(1)
        Integer altura
) {
    public static PatioDTO from(Patio p) {
        if (p == null) return null;
        return new PatioDTO(
                p.getId(),
                p.getNome(),
                p.getCapacidade(),
                p.getLocalizacao(),
                p.getLargura(),
                p.getAltura()
        );
    }

    public Patio toEntity() {
        Patio p = new Patio();
        p.setId(id);
        p.setNome(nome);
        p.setCapacidade(capacidade);
        p.setLocalizacao(localizacao);
        p.setLargura(largura != null ? largura : 15);
        p.setAltura(altura != null ? altura : 15);
        return p;
    }
}
