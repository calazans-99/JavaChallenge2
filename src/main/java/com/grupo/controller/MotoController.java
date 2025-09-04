package com.grupo.controller;

import com.grupo.dto.MotoDTO;
import com.grupo.exception.BusinessException;
import com.grupo.exception.ResourceNotFoundException;
import com.grupo.model.Moto;
import com.grupo.model.Patio;
import com.grupo.model.StatusMoto;
import com.grupo.repository.MotoRepository;
import com.grupo.repository.PatioRepository;
import com.grupo.repository.StatusMotoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/moto")
public class MotoController {

    @Autowired private MotoRepository motoRepository;
    @Autowired private PatioRepository patioRepository;
    @Autowired private StatusMotoRepository statusMotoRepository;

    @GetMapping
    public ResponseEntity<Page<MotoDTO>> listar(@PageableDefault(sort = "idMoto") Pageable pageable) {
        return ResponseEntity.ok(motoRepository.findAll(pageable).map(this::toDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MotoDTO> detalhar(@PathVariable Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Moto não encontrada"));
        return ResponseEntity.ok(toDTO(moto));
    }

    @PostMapping
    @CacheEvict(value = "motos", allEntries = true)
    public ResponseEntity<MotoDTO> criar(@RequestBody @Valid MotoDTO dto, UriComponentsBuilder uriBuilder) {
        if (motoRepository.existsByPlaca(dto.getPlaca())) {
            throw new BusinessException("Placa já cadastrada");
        }
        Patio patio = patioRepository.findById(dto.getIdPatio())
                .orElseThrow(() -> new ResourceNotFoundException("Pátio não encontrado"));
        StatusMoto status = statusMotoRepository.findById(dto.getIdStatusMoto())
                .orElseThrow(() -> new ResourceNotFoundException("Status da moto não encontrado"));

        Moto moto = new Moto();
        moto.setPlaca(dto.getPlaca());
        moto.setModelo(dto.getModelo());
        moto.setPatio(patio);
        moto.setStatusMoto(status);
        moto.setPosX(dto.getPosX());
        moto.setPosY(dto.getPosY());

        Moto salvo;
        try {
            salvo = motoRepository.save(moto);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("Violação de integridade ao salvar Moto");
        }

        URI location = uriBuilder.path("/moto/{id}").buildAndExpand(salvo.getIdMoto()).toUri();
        return ResponseEntity.created(location).body(toDTO(salvo));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "motos", allEntries = true)
    public ResponseEntity<MotoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid MotoDTO dto) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Moto não encontrada"));

        if (!moto.getPlaca().equalsIgnoreCase(dto.getPlaca()) &&
                motoRepository.existsByPlaca(dto.getPlaca())) {
            throw new BusinessException("Placa já cadastrada");
        }

        Patio patio = patioRepository.findById(dto.getIdPatio())
                .orElseThrow(() -> new ResourceNotFoundException("Pátio não encontrado"));
        StatusMoto status = statusMotoRepository.findById(dto.getIdStatusMoto())
                .orElseThrow(() -> new ResourceNotFoundException("Status da moto não encontrado"));

        moto.setPlaca(dto.getPlaca());
        moto.setModelo(dto.getModelo());
        moto.setPatio(patio);
        moto.setStatusMoto(status);
        moto.setPosX(dto.getPosX());
        moto.setPosY(dto.getPosY());

        Moto salvo = motoRepository.save(moto);
        return ResponseEntity.ok(toDTO(salvo));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "motos", allEntries = true)
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        Moto moto = motoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Moto não encontrada"));
        motoRepository.delete(moto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Cacheable("motos")
    public ResponseEntity<Page<MotoDTO>> buscar(@RequestParam(required = false) String placa,
                                                @PageableDefault(sort = "idMoto") Pageable pageable) {
        Page<MotoDTO> page = (placa != null && !placa.isBlank())
                ? motoRepository.findByPlacaContainingIgnoreCase(placa, pageable).map(this::toDTO)
                : motoRepository.findAll(pageable).map(this::toDTO);
        return ResponseEntity.ok(page);
    }

    private MotoDTO toDTO(Moto m) {
        MotoDTO dto = new MotoDTO();
        dto.setIdMoto(m.getIdMoto());
        dto.setPlaca(m.getPlaca());
        dto.setModelo(m.getModelo());
        dto.setIdPatio(m.getPatio() != null ? m.getPatio().getIdPatio() : null);
        dto.setIdStatusMoto(m.getStatusMoto() != null ? m.getStatusMoto().getIdStatus() : null);
        dto.setPosX(m.getPosX());
        dto.setPosY(m.getPosY());
        return dto;
    }
}
