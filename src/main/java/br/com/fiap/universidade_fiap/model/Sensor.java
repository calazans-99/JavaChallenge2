package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sensor")
    private Long id;

    @NotBlank
    @Size(max = 40)
    @Column(name = "tipo", nullable = false, length = 40)
    private String tipo;

    @NotBlank
    @Size(max = 20)
    @Column(name = "status", nullable = false, length = 20)
    private String status;

    @Min(0)
    @Column(name = "pos_x")
    private Integer posX;

    @Min(0)
    @Column(name = "pos_y")
    private Integer posY;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_patio", nullable = false)
    private Patio patio;

    public Sensor() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getPosX() { return posX; }
    public void setPosX(Integer posX) { this.posX = posX; }
    public Integer getPosY() { return posY; }
    public void setPosY(Integer posY) { this.posY = posY; }
    public Patio getPatio() { return patio; }
    public void setPatio(Patio patio) { this.patio = patio; }
}
