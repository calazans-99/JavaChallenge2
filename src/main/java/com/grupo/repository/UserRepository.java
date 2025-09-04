package com.grupo.repository;

import com.grupo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Page<User> findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String nome, String email, Pageable pageable);

    Optional<User> findByEmail(String email);
}
