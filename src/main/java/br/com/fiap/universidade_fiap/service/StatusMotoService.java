package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.repository.StatusMotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StatusMotoService {

    private final StatusMotoRepository repo;

    public StatusMotoService(StatusMotoRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<StatusMoto> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public StatusMoto findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Status da moto não encontrado"));
    }

    public StatusMoto save(StatusMoto status) {
        return repo.save(status);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public StatusMoto findByNomeOrThrow(String nome) {
        return repo.findByNome(nome)
                .orElseThrow(() -> new IllegalArgumentException("Status '" + nome + "' não existe"));
    }
}
