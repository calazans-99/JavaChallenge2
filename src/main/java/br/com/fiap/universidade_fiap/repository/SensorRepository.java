package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> { }