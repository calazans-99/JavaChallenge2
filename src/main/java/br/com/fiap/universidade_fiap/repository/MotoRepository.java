package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.Moto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MotoRepository extends JpaRepository<Moto, Long> {

    // Listagem já com pátio e status carregados (evita Lazy na view)
    @EntityGraph(attributePaths = {"patio", "status"})
    @Query("select m from Moto m")
    List<Moto> findAllWithPatioAndStatus();

    // Detalhe/edição já com pátio e status carregados
    @EntityGraph(attributePaths = {"patio", "status"})
    Optional<Moto> findById(Long id);
}
