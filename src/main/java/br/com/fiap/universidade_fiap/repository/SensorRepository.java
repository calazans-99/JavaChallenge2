package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.Sensor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    // Listagem já com o pátio carregado (evita Lazy na view)
    @EntityGraph(attributePaths = {"patio"})
    @Query("select s from Sensor s")
    List<Sensor> findAllWithPatio();

    // Detalhe/edição já com o pátio carregado
    @EntityGraph(attributePaths = {"patio"})
    Optional<Sensor> findById(Long id);
}
