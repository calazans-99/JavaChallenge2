package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "usuario",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_usuario_username", columnNames = "username")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    @Column(name = "username", nullable = false, unique = true, length = 255)
    private String username;

    /**
     * Observações:
     * - Mantemos apenas @Size para não travar edições em que a senha não será trocada.
     * - No create, sua tela já exige senha; e o Service deve aplicar BCrypt antes de salvar.
     */
    @Size(min = 6, max = 255, message = "Senha deve ter ao menos 6 caracteres")
    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "nome_perfil")
    private String nomePerfil;

    @Column(name = "img_perfil")
    private String imgPerfil;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_funcao_tab",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_funcao")
    )
    private Set<Funcao> funcoes = new HashSet<>();

    public User() { }

    public User(String username, String senha) {
        this.username = username;
        this.senha = senha;
    }

    /* Normalizações simples */
    @PrePersist @PreUpdate
    private void preSave() {
        if (username != null) username = username.trim();
        if (nomePerfil != null) nomePerfil = nomePerfil.trim();
        if (imgPerfil != null) imgPerfil = imgPerfil.trim();
    }

    // Getters/Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public String getNomePerfil() { return nomePerfil; }
    public void setNomePerfil(String nomePerfil) { this.nomePerfil = nomePerfil; }

    public String getImgPerfil() { return imgPerfil; }
    public void setImgPerfil(String imgPerfil) { this.imgPerfil = imgPerfil; }

    public Set<Funcao> getFuncoes() { return funcoes; }
    public void setFuncoes(Set<Funcao> funcoes) { this.funcoes = funcoes; }

    // equals/hashCode por id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User u)) return false;
        return id != null && id.equals(u.id);
    }
    @Override
    public int hashCode() { return 31; }
}
