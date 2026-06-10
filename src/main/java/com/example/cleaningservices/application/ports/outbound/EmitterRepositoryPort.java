package com.example.cleaningservices.application.ports.outbound;

import com.example.cleaningservices.domain.model.Emitter;
import java.util.List;
import java.util.Optional;

public interface EmitterRepositoryPort {
    Emitter save(Emitter emitter);
    List<Emitter> findAll();
    Optional<Emitter> findById(Long id);
    void deleteById(Long id);
    boolean existsByCpfCnpj(String cpfCnpj);
}

