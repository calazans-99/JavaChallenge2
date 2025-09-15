package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.Patio;
import br.com.fiap.universidade_fiap.model.Sensor;
import br.com.fiap.universidade_fiap.repository.SensorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SensorService {

    private final SensorRepository repo;

    public SensorService(SensorRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Sensor> findAll() {
        return repo.findAll();
    }

    @Transactional(readOnly = true)
    public Sensor findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sensor não encontrado"));
    }

    public Sensor save(Sensor sensor) {
        return repo.save(sensor);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    // utilidades
    @Transactional(readOnly = true)
    public List<Sensor> findByPatio(Patio patio) {
        return repo.findByPatio(patio);
    }

    @Transactional(readOnly = true)
    public List<Sensor> searchByTipo(String termo) {
        return repo.findByTipoContainingIgnoreCase(termo);
    }

    @Transactional(readOnly = true)
    public List<Sensor> searchByStatus(String termo) {
        return repo.findByStatusContainingIgnoreCase(termo);
    }
}
