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
        // Carrega 'patio' junto para evitar LazyInitializationException com open-in-view=false
        return repo.findAllByOrderByIdAsc();
        // ou: return repo.findAllWithPatio();
    }

    @Transactional(readOnly = true)
    public Sensor findById(Long id) {
        // Para o form/edição também evitar lazy
        return repo.findByIdWithPatio(id)
                .orElseThrow(() -> new EntityNotFoundException("Sensor não encontrado"));
        // Se não quiser o método acima, troque por:
        // return repo.findById(id).orElseThrow(...);
    }

    @Transactional
    public Sensor save(Sensor s) {
        return repo.save(s);
    }

    @Transactional
    public void delete(Long id) {
        repo.deleteById(id);
    }

    // ===== Fluxo 2: Ocupar / Liberar Sensor =====
    @Transactional
    public void ocupar(Long id) {
        var s = findById(id);
        s.setStatus("OCUPADO");
        repo.save(s);
    }

    @Transactional
    public void liberar(Long id) {
        var s = findById(id);
        s.setStatus("LIVRE");
        repo.save(s);
    }
}
