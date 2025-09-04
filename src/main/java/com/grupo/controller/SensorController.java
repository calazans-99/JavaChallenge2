package com.grupo.controller;

import com.grupo.dto.SensorDTO;
import com.grupo.exception.ResourceNotFoundException;
import com.grupo.model.Moto;
import com.grupo.model.Sensor;
import com.grupo.repository.MotoRepository;
import com.grupo.repository.SensorRepository;
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
@RequestMapping("/sensores")
public class SensorController {

    @Autowired private SensorRepository sensorRepository;
    @Autowired private MotoRepository motoRepository;

    @GetMapping
    public ResponseEntity<Page<SensorDTO>> listar(@PageableDefault(sort = "idSensor") Pageable pageable) {
        return ResponseEntity.ok(sensorRepository.findAll(pageable).map(this::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorDTO> detalhar(@PathVariable Long id) {
        Sensor s = sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado"));
        return ResponseEntity.ok(toDTO(s));
    }

    @PostMapping
    @CacheEvict(value = "sensores", allEntries = true)
    public ResponseEntity<SensorDTO> criar(@RequestBody @Valid SensorDTO dto, UriComponentsBuilder uriBuilder) {
        Moto moto = motoRepository.findById(dto.getIdMoto())
                .orElseThrow(() -> new ResourceNotFoundException("Moto não encontrada"));

        Sensor s = new Sensor();
        s.setMoto(moto);
        s.setTemperatura(dto.getTemperatura());
        s.setLigada(dto.getLigada());

        Sensor salvo = sensorRepository.save(s);
        URI location = uriBuilder.path("/sensores/{id}").buildAndExpand(salvo.getIdSensor()).toUri();
        return ResponseEntity.created(location).body(toDTO(salvo));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "sensores", allEntries = true)
    public ResponseEntity<SensorDTO> atualizar(@PathVariable Long id, @RequestBody @Valid SensorDTO dto) {
        Sensor s = sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado"));

        Moto moto = motoRepository.findById(dto.getIdMoto())
                .orElseThrow(() -> new ResourceNotFoundException("Moto não encontrada"));

        s.setMoto(moto);
        s.setTemperatura(dto.getTemperatura());
        s.setLigada(dto.getLigada());

        Sensor salvo = sensorRepository.save(s);
        return ResponseEntity.ok(toDTO(salvo));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "sensores", allEntries = true)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Sensor s = sensorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sensor não encontrado"));
        sensorRepository.delete(s);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/por-moto/{idMoto}")
    @Cacheable("sensores")
    public ResponseEntity<Page<SensorDTO>> listarPorMoto(@PathVariable Long idMoto,
                                                         @PageableDefault(sort = "idSensor") Pageable pageable) {
        Page<SensorDTO> page = sensorRepository.findByMoto_IdMoto(idMoto, pageable).map(this::toDTO);
        return ResponseEntity.ok(page);
    }

    private SensorDTO toDTO(Sensor s) {
        SensorDTO dto = new SensorDTO();
        dto.setIdSensor(s.getIdSensor());
        dto.setIdMoto(s.getMoto() != null ? s.getMoto().getIdMoto() : null);
        dto.setTemperatura(s.getTemperatura());
        dto.setLigada(s.getLigada());
        return dto;
    }
}
