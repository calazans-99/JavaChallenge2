package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.StatusMotoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MotoService {

    private final MotoRepository repo;
    private final StatusMotoRepository statusRepo;

    public MotoService(MotoRepository repo, StatusMotoRepository statusRepo) {
        this.repo = repo;
        this.statusRepo = statusRepo;
    }

    @Transactional(readOnly = true)
    public List<Moto> findAll() {
        // mantém o carregamento com EntityGraph (patio, status) no repo
        return repo.findAllWithPatioAndStatus();
    }

    @Transactional(readOnly = true)
    public Moto findById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Moto não encontrada"));
    }

    @Transactional
    public Moto save(Moto moto) {
        return repo.save(moto);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ===== Novo: ação de negócio para alterar o status =====
    @Transactional
    public void alterarStatus(Long idMoto, String novoStatusNome) {
        var moto = repo.findById(idMoto)
                .orElseThrow(() -> new EntityNotFoundException("Moto não encontrada"));

        var status = statusRepo.findByNome(novoStatusNome)
                .orElseThrow(() -> new EntityNotFoundException("Status não encontrado: " + novoStatusNome));

        moto.setStatus(status);
        repo.save(moto);
    }
}
