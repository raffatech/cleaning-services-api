package com.example.cleaningservices.application.ports.outbound;

import com.example.cleaningservices.domain.model.Quote;
import java.util.List;
import java.util.Optional;

// Define o que a aplicação precisa do banco para Orçamentos
public interface QuoteRepositoryPort {
    Quote save(Quote quote);
    List<Quote> findAll();
    Optional<Quote> findById(Long id);
    List<Quote> findByEmitterId(Long emitterId);
}

