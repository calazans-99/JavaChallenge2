package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}