package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.User;
import br.com.fiap.universidade_fiap.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
    }

    /** Criação com encode de senha obrigatório */
    public User create(User usuario) {
        usuario.setSenha(encoder.encode(usuario.getSenha()));
        return repo.save(usuario);
    }

    /**
     * Atualização segura:
     * - atualiza username, nomePerfil, imgPerfil e funcoes
     * - só re-encode se senha nova foi informada (alterarSenha = true)
     */
    public User update(Long id, User dados, boolean alterarSenha) {
        var db = findById(id);
        db.setUsername(dados.getUsername());
        db.setNomePerfil(dados.getNomePerfil());
        db.setImgPerfil(dados.getImgPerfil());
        db.setFuncoes(dados.getFuncoes());

        if (alterarSenha && dados.getSenha() != null && !dados.getSenha().isBlank()) {
            db.setSenha(encoder.encode(dados.getSenha()));
        }
        return repo.save(db);
    }

    /** Salva direto (se você já tratou senha no controller) */
    public User save(User usuario) {
        return repo.save(usuario);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return repo.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public User findByUsernameOrThrow(String username) {
        return repo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuário '" + username + "' não encontrado"));
    }
}
