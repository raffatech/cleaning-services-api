package com.example.cleaningservices.application.ports.inbound;

import com.example.cleaningservices.domain.model.Emitter;
import java.util.List;

public interface EmitterServicePort {
    Emitter createEmitter(Emitter emitter);
    List<Emitter> findAllEmitters();
    Emitter findEmitterById(Long id);
    Emitter updateEmitter(Long id, Emitter emitter);
    void deleteEmitter(Long id);
}

