package br.com.fiap.universidade_fiap.dto;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.Patio;
import br.com.fiap.universidade_fiap.model.StatusMoto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MotoDTO(
        Long id,
        @NotBlank @Size(max = 120) String modelo,
        @NotBlank @Size(max = 20)  String placa,
        Integer posX,
        Integer posY,
        Long patioId,
        Long statusId
) {
    public static MotoDTO from(Moto m) {
        return new MotoDTO(
                m.getId(),
                m.getModelo(),
                m.getPlaca(),
                m.getPosX(),
                m.getPosY(),
                m.getPatio() != null ? m.getPatio().getId() : null,
                m.getStatus() != null ? m.getStatus().getId() : null
        );
    }

    public Moto toEntity() {
        Moto m = new Moto();
        m.setId(id);
        m.setModelo(modelo);
        m.setPlaca(placa);
        m.setPosX(posX);
        m.setPosY(posY);
        if (patioId != null) {
            Patio p = new Patio(); p.setId(patioId);
            m.setPatio(p);
        }
        if (statusId != null) {
            StatusMoto s = new StatusMoto(); s.setId(statusId);
            m.setStatus(s);
        }
        return m;
    }
}
