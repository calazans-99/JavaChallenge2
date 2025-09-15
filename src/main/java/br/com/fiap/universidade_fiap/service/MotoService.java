package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.Patio;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MotoService {

    private final MotoRepository repo;

    public MotoService(MotoRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Moto> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Moto findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Moto não encontrada"));
    }

    public Moto save(Moto moto) {
        return repo.save(moto);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // utilidades
    @Transactional(readOnly = true)
    public boolean existsByPlaca(String placa) {
        return repo.findByPlaca(placa).isPresent();
    }

    @Transactional(readOnly = true)
    public List<Moto> searchByModelo(String termo) {
        return repo.findByModeloContainingIgnoreCase(termo);
    }

    @Transactional(readOnly = true)
    public List<Moto> findByPatio(Patio patio) {
        return repo.findByPatio(patio);
    }
}
