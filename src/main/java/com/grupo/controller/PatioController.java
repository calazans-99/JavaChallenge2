package com.grupo.controller;

import com.grupo.dto.PatioDTO;
import com.grupo.exception.BusinessException;
import com.grupo.exception.ResourceNotFoundException;
import com.grupo.model.Patio;
import com.grupo.repository.PatioRepository;
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
@RequestMapping("/patios")
public class PatioController {

    @Autowired private PatioRepository patioRepository;

    @GetMapping
    public ResponseEntity<Page<PatioDTO>> listar(@PageableDefault(sort = "idPatio") Pageable pageable) {
        return ResponseEntity.ok(patioRepository.findAll(pageable).map(this::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatioDTO> detalhar(@PathVariable Long id) {
        Patio p = patioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio não encontrado"));
        return ResponseEntity.ok(toDTO(p));
    }

    @PostMapping
    @CacheEvict(value = "patios", allEntries = true)
    public ResponseEntity<PatioDTO> criar(@RequestBody @Valid PatioDTO dto, UriComponentsBuilder uriBuilder) {
        // exemplo: nome + cidade únicos por regra de negócio
        if (patioRepository.existsByNomeIgnoreCaseAndCidadeIgnoreCase(dto.getNome(), dto.getCidade())) {
            throw new BusinessException("Já existe um pátio com esse nome nessa cidade");
        }
        Patio p = new Patio();
        p.setNome(dto.getNome());
        p.setCidade(dto.getCidade());
        p.setLayout(dto.getLayout());

        Patio salvo = patioRepository.save(p);
        URI location = uriBuilder.path("/patios/{id}").buildAndExpand(salvo.getIdPatio()).toUri();
        return ResponseEntity.created(location).body(toDTO(salvo));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "patios", allEntries = true)
    public ResponseEntity<PatioDTO> atualizar(@PathVariable Long id, @RequestBody @Valid PatioDTO dto) {
        Patio p = patioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio não encontrado"));

        // se mudou nome/cidade, valida duplicidade
        if ((!p.getNome().equalsIgnoreCase(dto.getNome()) || !p.getCidade().equalsIgnoreCase(dto.getCidade())) &&
                patioRepository.existsByNomeIgnoreCaseAndCidadeIgnoreCase(dto.getNome(), dto.getCidade())) {
            throw new BusinessException("Já existe um pátio com esse nome nessa cidade");
        }

        p.setNome(dto.getNome());
        p.setCidade(dto.getCidade());
        p.setLayout(dto.getLayout());

        Patio salvo = patioRepository.save(p);
        return ResponseEntity.ok(toDTO(salvo));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "patios", allEntries = true)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Patio p = patioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pátio não encontrado"));
        patioRepository.delete(p);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Cacheable("patios")
    public ResponseEntity<Page<PatioDTO>> buscar(@RequestParam(required = false) String nome,
                                                 @PageableDefault(sort = "idPatio") Pageable pageable) {
        Page<PatioDTO> page = (nome != null && !nome.isBlank())
                ? patioRepository.findByNomeContainingIgnoreCase(nome, pageable).map(this::toDTO)
                : patioRepository.findAll(pageable).map(this::toDTO);
        return ResponseEntity.ok(page);
    }

    private PatioDTO toDTO(Patio p) {
        PatioDTO dto = new PatioDTO();
        dto.setIdPatio(p.getIdPatio());
        dto.setNome(p.getNome());
        dto.setCidade(p.getCidade());
        dto.setLayout(p.getLayout());
        return dto;
    }
}
