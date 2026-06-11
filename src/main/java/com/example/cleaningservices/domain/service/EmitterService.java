package com.example.cleaningservices.domain.service;

import com.example.cleaningservices.application.ports.inbound.EmitterServicePort;
import com.example.cleaningservices.application.ports.outbound.EmitterRepositoryPort;
import com.example.cleaningservices.domain.exception.EmitterNotFoundException;
import com.example.cleaningservices.domain.exception.ValidationException;
import com.example.cleaningservices.domain.model.Emitter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmitterService implements EmitterServicePort {

    private final EmitterRepositoryPort emitterRepositoryPort;

    public EmitterService(EmitterRepositoryPort emitterRepositoryPort) {
        this.emitterRepositoryPort = emitterRepositoryPort;
    }

    // valida se já existe emitente com o mesmo CPF/CNPJ
    // id null = create | id com valor = update (ignora o próprio emitente)
    private void validateEmitter(Emitter emitter, Long id) {
        Map<String, String> errors = new HashMap<>();

        Emitter existing = emitterRepositoryPort.findById(id != null ? id : -1L).orElse(null);

        if (emitterRepositoryPort.existsByCpfCnpj(emitter.getCpfCnpj())) {
            if (existing == null || !existing.getCpfCnpj().equals(emitter.getCpfCnpj())) {
                errors.put("cpfCnpj", "CPF/CNPJ: " + emitter.getCpfCnpj() + " already exists.");
            }
        }

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }

    @Override
    public Emitter createEmitter(Emitter emitter) {
        validateEmitter(emitter, null);
        return emitterRepositoryPort.save(emitter);
    }

    @Override
    public List<Emitter> findAllEmitters() {
        return emitterRepositoryPort.findAll();
    }

    @Override
    public Emitter findEmitterById(Long id) {
        return emitterRepositoryPort.findById(id)
                .orElseThrow(() -> new EmitterNotFoundException(id));
    }

    @Override
    public Emitter updateEmitter(Long id, Emitter emitter) {
        findEmitterById(id);
        emitter.setId(id);
        validateEmitter(emitter, id);
        return emitterRepositoryPort.save(emitter);
    }

    @Override
    public void deleteEmitter(Long id) {
        findEmitterById(id);
        emitterRepositoryPort.deleteById(id);
    }
}

