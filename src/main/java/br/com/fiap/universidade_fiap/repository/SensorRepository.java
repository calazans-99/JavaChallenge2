package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.Sensor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    // Lista com o Pátio carregado (evita LazyInitialization na view)
    @EntityGraph(attributePaths = {"patio"})
    List<Sensor> findAllByOrderByIdAsc();

    // (Opcional) detalhe com fetch join para o form/edição
    @Query("select s from Sensor s left join fetch s.patio where s.id = :id")
    Optional<Sensor> findByIdWithPatio(@Param("id") Long id);

    // Se preferir, você pode usar JPQL também para a lista:
    // @Query("select s from Sensor s left join fetch s.patio")
    // List<Sensor> findAllWithPatio();
}
