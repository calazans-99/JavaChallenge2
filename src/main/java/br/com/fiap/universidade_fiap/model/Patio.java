package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "patio")
public class Patio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String nome;

    @Min(0)
    @Column(nullable = false)
    private Integer capacidade;

    @OneToMany(mappedBy = "patio", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Moto> motos = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Integer getCapacidade() { return capacidade; }
    public void setCapacidade(Integer capacidade) { this.capacidade = capacidade; }
    public List<Moto> getMotos() { return motos; }
    public void setMotos(List<Moto> motos) { this.motos = motos; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patio)) return false;
        Patio patio = (Patio) o;
        return Objects.equals(id, patio.id);
    }
    @Override public int hashCode() { return Objects.hash(id); }
}