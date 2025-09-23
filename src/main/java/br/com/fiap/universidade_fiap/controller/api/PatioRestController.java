package br.com.fiap.universidade_fiap.controller.api;

import br.com.fiap.universidade_fiap.dto.PatioDTO;
import br.com.fiap.universidade_fiap.model.Patio;
import br.com.fiap.universidade_fiap.service.PatioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/patios")
public class PatioRestController {

    private final PatioService service;

    public PatioRestController(PatioService service) {
        this.service = service;
    }

    @GetMapping
    public List<PatioDTO> list() {
        return service.findAll().stream().map(PatioDTO::from).toList();
    }

    @GetMapping("/{id}")
    public PatioDTO get(@PathVariable Long id) {
        return PatioDTO.from(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PatioDTO> create(@Valid @RequestBody PatioDTO dto) {
        Patio saved = service.save(dto.toEntity());
        return ResponseEntity
                .created(URI.create("/api/v1/patios/" + saved.getId()))
                .body(PatioDTO.from(saved));
    }

    @PutMapping("/{id}")
    public PatioDTO update(@PathVariable Long id, @Valid @RequestBody PatioDTO dto) {
        Patio existing = service.findById(id); // garante que existe, com created_at preenchido
        existing.setNome(dto.nome());
        existing.setCapacidade(dto.capacidade());
        existing.setLocalizacao(dto.localizacao());
        existing.setLargura(dto.largura());
        existing.setAltura(dto.altura());
        // NÃO tocar em existing.setCreatedAt(...)
        return PatioDTO.from(service.save(existing));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
