package com.grupo.repository;

import com.grupo.model.Sensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Page<Sensor> findByMoto_IdMoto(Long idMoto, Pageable pageable);
}
