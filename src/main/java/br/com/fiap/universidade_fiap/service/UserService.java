package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.User;
import br.com.fiap.universidade_fiap.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return repo.findAllWithFuncoes(); // evita Lazy em usuario/list.html
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    @Transactional
    public User create(User u) {
        u.setSenha(encoder.encode(u.getSenha()));
        return repo.save(u);
    }

    @Transactional
    public User update(Long id, User u, boolean trocarSenha) {
        var atual = findById(id);
        atual.setUsername(u.getUsername());
        atual.setNomePerfil(u.getNomePerfil());
        atual.setImgPerfil(u.getImgPerfil());
        atual.setFuncoes(u.getFuncoes());
        if (trocarSenha) {
            atual.setSenha(encoder.encode(u.getSenha()));
        }
        return repo.save(atual);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
