package br.com.fiap.universidade_fiap.repository;

import br.com.fiap.universidade_fiap.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"funcoes"})
    @Query("select u from User u")
    List<User> findAllWithFuncoes();

    @EntityGraph(attributePaths = {"funcoes"})
    Optional<User> findById(Long id);

    // Opcional: já trazer funções no login e em telas que mostram perfis
    @EntityGraph(attributePaths = {"funcoes"})
    Optional<User> findByUsername(String username);
}
