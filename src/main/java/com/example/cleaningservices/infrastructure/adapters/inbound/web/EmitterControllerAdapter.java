package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import com.example.cleaningservices.application.ports.inbound.EmitterServicePort;
import com.example.cleaningservices.domain.model.Emitter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emitters")
public class EmitterControllerAdapter {

    private final EmitterServicePort emitterServicePort;

    public EmitterControllerAdapter(EmitterServicePort emitterServicePort) {
        this.emitterServicePort = emitterServicePort;
    }

    private Emitter convertToDomain(EmitterRequest request) {
        Emitter emitter = new Emitter();
        emitter.setCompanyName(request.getCompanyName());
        emitter.setSignerName(request.getSignerName());
        emitter.setCpfCnpj(request.getCpfCnpj());
        emitter.setTemplatePath(request.getTemplatePath());
        emitter.setMunicipalRegistration(request.getMunicipalRegistration());
        emitter.setPhone(request.getPhone());
        emitter.setEmail(request.getEmail());
        emitter.setAddress(request.getAddress());
        emitter.setLogoPath(request.getLogoPath());
        return emitter;
    }

    private EmitterResponse convertToResponse(Emitter emitter) {
        EmitterResponse response = new EmitterResponse();
        response.setId(emitter.getId());
        response.setCompanyName(emitter.getCompanyName());
        response.setSignerName(emitter.getSignerName());
        response.setCpfCnpj(emitter.getCpfCnpj());
        response.setTemplatePath(emitter.getTemplatePath());
        response.setMunicipalRegistration(emitter.getMunicipalRegistration());
        response.setPhone(emitter.getPhone());
        response.setEmail(emitter.getEmail());
        response.setAddress(emitter.getAddress());
        response.setLogoPath(emitter.getLogoPath());
        return response;
    }

    @PostMapping
    public ResponseEntity<EmitterResponse> create(@Valid @RequestBody EmitterRequest request) {
        Emitter emitter = convertToDomain(request);
        Emitter created = emitterServicePort.createEmitter(emitter);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(created));
    }

    @GetMapping
    public ResponseEntity<List<EmitterResponse>> findAll() {
        List<Emitter> emitters = emitterServicePort.findAllEmitters();
        List<EmitterResponse> responses = emitters.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmitterResponse> findById(@PathVariable Long id) {
        Emitter emitter = emitterServicePort.findEmitterById(id);
        return ResponseEntity.ok(convertToResponse(emitter));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmitterResponse> update(@PathVariable Long id, @Valid @RequestBody EmitterRequest request) {
        Emitter emitter = convertToDomain(request);
        Emitter updated = emitterServicePort.updateEmitter(id, emitter);
        return ResponseEntity.ok(convertToResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        emitterServicePort.deleteEmitter(id);
        return ResponseEntity.noContent().build();
    }
}
