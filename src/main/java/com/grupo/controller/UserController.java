package com.grupo.controller;

import com.grupo.dto.UserDTO;
import com.grupo.exception.BusinessException;
import com.grupo.exception.ResourceNotFoundException;
import com.grupo.model.User;
import com.grupo.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired private UserRepository userRepository;

    // GET /usuarios (paginado)
    @GetMapping
    public ResponseEntity<Page<UserDTO>> listar(@PageableDefault(sort = "id") Pageable pageable) {
        return ResponseEntity.ok(userRepository.findAll(pageable).map(this::toDTO));
    }

    // GET /usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> detalhar(@PathVariable Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return ResponseEntity.ok(toDTO(u));
    }

    // POST /usuarios
    @PostMapping
    @CacheEvict(value = "usuarios", allEntries = true)
    public ResponseEntity<UserDTO> criar(@RequestBody @Valid UserDTO dto, UriComponentsBuilder uriBuilder) {
        if (userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new BusinessException("Já existe um usuário com esse email");
        }

        User u = new User();
        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setTipoUsuario(dto.getTipoUsuario());

        User salvo = userRepository.save(u);
        URI location = uriBuilder.path("/usuarios/{id}").buildAndExpand(salvo.getId()).toUri();
        return ResponseEntity.created(location).body(toDTO(salvo));
    }

    // PUT /usuarios/{id}
    @PutMapping("/{id}")
    @CacheEvict(value = "usuarios", allEntries = true)
    public ResponseEntity<UserDTO> atualizar(@PathVariable Long id, @RequestBody @Valid UserDTO dto) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!u.getEmail().equalsIgnoreCase(dto.getEmail())
                && userRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new BusinessException("Já existe um usuário com esse email");
        }

        u.setNome(dto.getNome());
        u.setEmail(dto.getEmail());
        u.setTipoUsuario(dto.getTipoUsuario());

        User salvo = userRepository.save(u);
        return ResponseEntity.ok(toDTO(salvo));
    }

    // DELETE /usuarios/{id}
    @DeleteMapping("/{id}")
    @CacheEvict(value = "usuarios", allEntries = true)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        User u = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        userRepository.delete(u);
        return ResponseEntity.noContent().build();
    }

    // GET /usuarios/buscar?nomeOuEmail=...
    @GetMapping("/buscar")
    @Cacheable("usuarios")
    public ResponseEntity<Page<UserDTO>> buscar(@RequestParam(required = false) String nomeOuEmail,
                                                @PageableDefault(sort = "id") Pageable pageable) {
        Page<UserDTO> page = (nomeOuEmail != null && !nomeOuEmail.isBlank())
                ? userRepository.findByNomeContainingIgnoreCaseOrEmailContainingIgnoreCase(
                nomeOuEmail, nomeOuEmail, pageable).map(this::toDTO)
                : userRepository.findAll(pageable).map(this::toDTO);
        return ResponseEntity.ok(page);
    }

    // Mapper Entity -> DTO
    private UserDTO toDTO(User u) {
        UserDTO dto = new UserDTO();
        dto.setId(u.getId());
        dto.setNome(u.getNome());
        dto.setEmail(u.getEmail());
        dto.setTipoUsuario(u.getTipoUsuario());
        return dto;
    }
}
