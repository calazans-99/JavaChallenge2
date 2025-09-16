package br.com.fiap.universidade_fiap.service;

import br.com.fiap.universidade_fiap.model.Sensor;
import br.com.fiap.universidade_fiap.repository.SensorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SensorService {

    private final SensorRepository repo;

    public SensorService(SensorRepository repo) {
        this.repo = repo;
    }

    @Transactional(readOnly = true)
    public List<Sensor> findAll() {
        // evita LazyInitializationException na lista (s.patio.nome)
        return repo.findAllWithPatio();
    }

    @Transactional(readOnly = true)
    public Sensor findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));
    }

    @Transactional
    public Sensor save(Sensor sensor) {
        return repo.save(sensor);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
