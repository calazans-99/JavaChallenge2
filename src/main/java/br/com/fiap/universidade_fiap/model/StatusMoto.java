package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "status_moto")
public class StatusMoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_status_moto")
    private Long id;

    @NotBlank
    @Size(max = 40)
    @Column(name = "nome", nullable = false, unique = true, length = 40)
    private String nome; // ATIVA, EM_MANUTENCAO, INATIVA...

    public StatusMoto() {}
    public StatusMoto(String nome) { this.nome = nome; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    // ajuda o <select th:field="*{status}">
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StatusMoto)) return false;
        StatusMoto other = (StatusMoto) o;
        return id != null && id.equals(other.id);
    }
    @Override public int hashCode() { return Objects.hashCode(id); }
}
