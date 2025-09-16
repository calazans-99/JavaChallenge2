package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MotoService {

    private final MotoRepository repo;

    public MotoService(MotoRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Moto> findAll() {
        // evita LazyInitializationException na lista (m.patio.nome / m.status.nome)
        return repo.findAllWithPatioAndStatus();
    }

    @Transactional(readOnly = true)
    public Moto findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada"));
    }

    @Transactional
    public Moto save(Moto moto) {
        return repo.save(moto);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
