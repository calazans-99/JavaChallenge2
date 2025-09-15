package br.com.fiap.universidade_fiap.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import br.com.fiap.universidade_fiap.repository.UserRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public UsuarioDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Funcao.nome é String: "ADMIN", "PROFESSOR", ...
        var authorities = u.getFuncoes().stream()
                .map(f -> new SimpleGrantedAuthority(f.getNome()))
                .collect(Collectors.toSet());

        return org.springframework.security.core.userdetails.User
                .withUsername(u.getUsername())
                .password(u.getSenha())  // usa coluna 'senha'
                .authorities(authorities)
                .accountLocked(false)
                .disabled(false)
                .build();
    }
}
