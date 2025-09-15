package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // conforme migrações
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "username", nullable = false, unique = true, length = 255)
    private String username;

    @NotBlank
    @Column(name = "senha", nullable = false) // nome de coluna do seu schema
    private String senha;                      // (antes alguns lugares usavam "password")

    @Size(max = 255)
    @Column(name = "nome_perfil", length = 255)
    private String nomePerfil;

    @Size(max = 255)
    @Column(name = "img_perfil", length = 255)
    private String imgPerfil;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_funcao_tab",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_funcao")
    )
    private Set<Funcao> funcoes = new HashSet<>();

    public User() {}

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

    public void addFuncao(Funcao f) { this.funcoes.add(f); }
}
