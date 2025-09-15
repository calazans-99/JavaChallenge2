package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MotoRepository extends JpaRepository<Moto, Long> {

    // validação de duplicidade de placa
    Optional<Moto> findByPlaca(String placa);

    // filtros comuns
    List<Moto> findByModeloContainingIgnoreCase(String modelo);
    List<Moto> findByPatio(Patio patio);
    List<Moto> findByStatusNome(String nome); // navegação pela relação (status.nome)
}
