package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patio")
public class Patio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patio")
    private Long id;

    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    @Column(name = "capacidade", nullable = false)
    private Integer capacidade;

    @Column(name = "localizacao", length = 255)
    private String localizacao;

    // dimensões do grid usadas pelo app mobile
    @Column(name = "largura", nullable = false)
    private Integer largura = 15;

    @Column(name = "altura", nullable = false)
    private Integer altura = 15;

    // auditoria
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (largura == null) largura = 15;
        if (altura == null)  altura  = 15;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public Integer getLargura() { return largura; }
    public void setLargura(Integer largura) { this.largura = largura; }

    public Integer getAltura() { return altura; }
    public void setAltura(Integer altura) { this.altura = altura; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
