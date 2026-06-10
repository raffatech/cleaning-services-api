package com.example.cleaningservices.domain.exception;

public class EmitterNotFoundException extends RuntimeException {
    public EmitterNotFoundException(Long id) {
        super("Emitter not found with id: " + id);
    }
}

