package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.Funcao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FuncaoRepository extends JpaRepository<Funcao, Long> {

    Optional<Funcao> findByNome(String nome);

    boolean existsByNome(String nome);
}
