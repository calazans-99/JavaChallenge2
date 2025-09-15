package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.StatusMoto;
import br.com.fiap.universidade_fiap.repository.StatusMotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StatusService {

    private final StatusMotoRepository repo;

    public StatusService(StatusMotoRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<StatusMoto> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public StatusMoto findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Status não encontrado"));
    }

    public StatusMoto save(StatusMoto s) {
        return repo.save(s);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
