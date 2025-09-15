package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatioRepository extends JpaRepository<Patio, Long> {

    // busca por nome (para filtro na listagem)
    List<Patio> findByNomeContainingIgnoreCase(String nome);
}
