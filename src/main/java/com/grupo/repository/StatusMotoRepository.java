package com.grupo.repository;

import com.grupo.model.StatusMoto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusMotoRepository extends JpaRepository<StatusMoto, Long> {
    Page<StatusMoto> findByDescricaoContainingIgnoreCase(String descricao, Pageable pageable);
    boolean existsByDescricaoIgnoreCase(String descricao);
}
