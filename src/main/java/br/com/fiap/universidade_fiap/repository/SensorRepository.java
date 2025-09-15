package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.Sensor;
import br.com.fiap.universidade_fiap.model.Patio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

    // todos os sensores de um pátio
    List<Sensor> findByPatio(Patio patio);

    // filtros úteis na listagem
    List<Sensor> findByTipoContainingIgnoreCase(String tipo);
    List<Sensor> findByStatusContainingIgnoreCase(String status);
}
