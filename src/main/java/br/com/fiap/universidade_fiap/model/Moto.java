package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(
        name = "moto",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_moto_placa", columnNames = "placa")
        }
)
public class Moto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_moto")
    private Long id;

    @NotBlank
    @Size(max = 50)
    @Column(name = "modelo", nullable = false, length = 50)
    private String modelo;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{3}-?\\d{4}$", message = "Placa no formato ABC-1234")
    @Size(max = 8)
    @Column(name = "placa", nullable = false, unique = true, length = 8)
    private String placa;

    @Min(0)
    @Column(name = "pos_x")
    private Integer posX;

    @Min(0)
    @Column(name = "pos_y")
    private Integer posY;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patio", nullable = false)
    private Patio patio;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_status_moto", nullable = false)
    private StatusMoto status;

    public Moto() {}

    public Moto(String modelo, String placa) {
        this.modelo = modelo;
        this.placa = placa;
    }

    /* Normalizações simples antes de salvar/atualizar */
    @PrePersist @PreUpdate
    private void preSave() {
        if (placa != null) placa = placa.trim().toUpperCase();
        if (modelo != null) modelo = modelo.trim();
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public Integer getPosX() { return posX; }
    public void setPosX(Integer posX) { this.posX = posX; }

    public Integer getPosY() { return posY; }
    public void setPosY(Integer posY) { this.posY = posY; }

    public Patio getPatio() { return patio; }
    public void setPatio(Patio patio) { this.patio = patio; }

    public StatusMoto getStatus() { return status; }
    public void setStatus(StatusMoto status) { this.status = status; }

    /* equals/hashCode baseados no id */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Moto m)) return false;
        return id != null && id.equals(m.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
