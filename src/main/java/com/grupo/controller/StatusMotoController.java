package com.grupo.controller;

import com.grupo.dto.StatusMotoDTO;
import com.grupo.exception.BusinessException;
import com.grupo.exception.ResourceNotFoundException;
import com.grupo.model.StatusMoto;
import com.grupo.repository.StatusMotoRepository;
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
@RequestMapping("/status-motos")
public class StatusMotoController {

    @Autowired
    private StatusMotoRepository statusMotoRepository;

    // GET /status-motos (paginado)
    @GetMapping
    @Cacheable("status")
    public ResponseEntity<Page<StatusMotoDTO>> listar(
            @PageableDefault(sort = "idStatus") Pageable pageable) {
        Page<StatusMotoDTO> page = statusMotoRepository.findAll(pageable).map(this::toDTO);
        return ResponseEntity.ok(page);
    }

    // GET /status-motos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<StatusMotoDTO> detalhar(@PathVariable Long id) {
        StatusMoto s = statusMotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status da moto não encontrado"));
        return ResponseEntity.ok(toDTO(s));
    }

    // POST /status-motos
    @PostMapping
    @CacheEvict(value = "status", allEntries = true)
    public ResponseEntity<StatusMotoDTO> criar(@RequestBody @Valid StatusMotoDTO dto,
                                               UriComponentsBuilder uriBuilder) {
        // Regra simples: evitar descrição duplicada (case-insensitive)
        if (statusMotoRepository.existsByDescricaoIgnoreCase(dto.getDescricao())) {
            throw new BusinessException("Já existe um status com essa descrição");
        }

        StatusMoto s = new StatusMoto();
        s.setDescricao(dto.getDescricao());

        StatusMoto salvo = statusMotoRepository.save(s);
        URI location = uriBuilder.path("/status-motos/{id}")
                .buildAndExpand(salvo.getIdStatus())
                .toUri();

        return ResponseEntity.created(location).body(toDTO(salvo));
    }

    // PUT /status-motos/{id}
    @PutMapping("/{id}")
    @CacheEvict(value = "status", allEntries = true)
    public ResponseEntity<StatusMotoDTO> atualizar(@PathVariable Long id,
                                                   @RequestBody @Valid StatusMotoDTO dto) {
        StatusMoto s = statusMotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status da moto não encontrado"));

        // Se mudou a descrição, valida duplicidade
        if (!s.getDescricao().equalsIgnoreCase(dto.getDescricao())
                && statusMotoRepository.existsByDescricaoIgnoreCase(dto.getDescricao())) {
            throw new BusinessException("Já existe um status com essa descrição");
        }

        s.setDescricao(dto.getDescricao());
        StatusMoto salvo = statusMotoRepository.save(s);
        return ResponseEntity.ok(toDTO(salvo));
    }

    // DELETE /status-motos/{id}
    @DeleteMapping("/{id}")
    @CacheEvict(value = "status", allEntries = true)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        StatusMoto s = statusMotoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Status da moto não encontrado"));
        statusMotoRepository.delete(s);
        return ResponseEntity.noContent().build();
    }

    // GET /status-motos/buscar?descricao=...(paginado)
    @GetMapping("/buscar")
    @Cacheable("status")
    public ResponseEntity<Page<StatusMotoDTO>> buscar(
            @RequestParam(required = false) String descricao,
            @PageableDefault(sort = "idStatus") Pageable pageable) {

        Page<StatusMotoDTO> page = (descricao != null && !descricao.isBlank())
                ? statusMotoRepository.findByDescricaoContainingIgnoreCase(descricao, pageable).map(this::toDTO)
                : statusMotoRepository.findAll(pageable).map(this::toDTO);

        return ResponseEntity.ok(page);
    }

    // Mapper Entity -> DTO
    private StatusMotoDTO toDTO(StatusMoto s) {
        StatusMotoDTO dto = new StatusMotoDTO();
        dto.setIdStatus(s.getIdStatus());
        dto.setDescricao(s.getDescricao());
        return dto;
    }
}
