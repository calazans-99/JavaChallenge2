package br.com.fiap.universidade_fiap.controller.api;

import br.com.fiap.universidade_fiap.dto.MotoDTO;
import br.com.fiap.universidade_fiap.model.Moto;
import br.com.fiap.universidade_fiap.service.MotoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/motos")
public class MotoRestController {

    private final MotoService service;

    public MotoRestController(MotoService service) {
        this.service = service;
    }

    @GetMapping
    public List<MotoDTO> list() {
        return service.findAll().stream().map(MotoDTO::from).toList();
    }

    @GetMapping("/{id}")
    public MotoDTO get(@PathVariable Long id) {
        Moto m = service.findById(id); // assuma que lança exceção se não achar
        return MotoDTO.from(m);
    }

    @PostMapping
    public ResponseEntity<MotoDTO> create(@Valid @RequestBody MotoDTO dto) {
        Moto saved = service.save(dto.toEntity());
        return ResponseEntity
                .created(URI.create("/api/v1/motos/" + saved.getId()))
                .body(MotoDTO.from(saved));
    }

    @PutMapping("/{id}")
    public MotoDTO update(@PathVariable Long id, @Valid @RequestBody MotoDTO dto) {
        Moto entity = dto.toEntity();
        entity.setId(id);
        return MotoDTO.from(service.save(entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
