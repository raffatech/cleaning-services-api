package com.example.cleaningservices.domain.exception;

public class QuoteNotFoundException extends RuntimeException {
    public QuoteNotFoundException(Long id) {
        super("Quote not found with id: " + id);
    }
}

