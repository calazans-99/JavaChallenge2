package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "moto")
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
}
