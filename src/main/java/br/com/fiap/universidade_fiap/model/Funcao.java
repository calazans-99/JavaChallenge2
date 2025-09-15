package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "funcao")
public class Funcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // conforme as migrações
    private Long id;

    @NotBlank
    @Size(max = 40)
    @Column(name = "nome", nullable = false, unique = true, length = 40)
    private String nome; // ex.: "ADMIN", "PROFESSOR", "DISCENTE", ...

    public Funcao() {}
    public Funcao(String nome) { this.nome = nome; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    @Transient
    public String getAuthority() { return nome; }

    // importante p/ selects do Thymeleaf (ManyToMany em Usuario)
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Funcao)) return false;
        Funcao other = (Funcao) o;
        if (id != null && other.id != null) return Objects.equals(id, other.id);
        return Objects.equals(nome, other.nome);
    }
    @Override public int hashCode() {
        return (id != null) ? Objects.hash(id) : Objects.hash(nome);
    }

    @Override public String toString() {
        return "Funcao{id=" + id + ", nome='" + nome + "'}";
    }
}
