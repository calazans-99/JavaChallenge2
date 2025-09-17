package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.StatusMoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusMotoRepository extends JpaRepository<StatusMoto, Long> {
    Optional<StatusMoto> findByNome(String nome);
}
