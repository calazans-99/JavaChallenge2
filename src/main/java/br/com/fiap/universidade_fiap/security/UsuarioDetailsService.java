package br.com.fiap.universidade_fiap.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fiap.universidade_fiap.repository.UserRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    private final UserRepository repo;

    public UsuarioDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var u = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        // Prefixa com ROLE_ para funcionar com .hasRole(...) no Security
        Set<SimpleGrantedAuthority> authorities = u.getFuncoes() == null ? Set.of() :
                u.getFuncoes().stream()
                        .map(f -> "ROLE_" + (f.getNome() == null ? "" : f.getNome().trim().toUpperCase()))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());

        return User.withUsername(u.getUsername())
                .password(u.getSenha()) // bcrypt já armazenado
                .authorities(authorities)
                .accountLocked(false)
                .disabled(false)
                .build();
    }
}
