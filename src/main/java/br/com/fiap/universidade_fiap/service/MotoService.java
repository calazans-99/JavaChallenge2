package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.Patio;
import br.com.fiap.universidade_fiap.repository.MotoRepository;
import br.com.fiap.universidade_fiap.repository.PatioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MotoService {
    private final MotoRepository motoRepo;
    private final PatioRepository patioRepo;

    public MotoService(MotoRepository motoRepo, PatioRepository patioRepo) {
        this.motoRepo = motoRepo;
        this.patioRepo = patioRepo;
    }

    @Transactional
    public Moto salvarComValidacao(Moto m) {
        if (m.getPatio() != null && m.getPatio().getId() != null) {
            Patio patio = patioRepo.findById(m.getPatio().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Pátio inválido"));
            long qtd = motoRepo.countByPatio(patio);
            if (qtd >= patio.getCapacidade()) {
                throw new IllegalStateException("Pátio lotado! Não é possível adicionar a moto.");
            }
            m.setPatio(patio);
        }
        return motoRepo.save(m);
    }
}