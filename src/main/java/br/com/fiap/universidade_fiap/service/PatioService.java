package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.Patio;
import br.com.fiap.universidade_fiap.repository.PatioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatioService {

    private final PatioRepository repo;

    public PatioService(PatioRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Patio> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Patio findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pátio não encontrado"));
    }

    public Patio save(Patio patio) {
        return repo.save(patio);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Patio> searchByNome(String termo) {
        return repo.findByNomeContainingIgnoreCase(termo);
    }
}
