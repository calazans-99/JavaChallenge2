package com.grupo.repository;

import com.grupo.model.Patio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatioRepository extends JpaRepository<Patio, Long> {
    Page<Patio> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    boolean existsByNomeIgnoreCaseAndCidadeIgnoreCase(String nome, String cidade);
}
