package com.example.cleaningservices.application.ports.outbound;

import com.example.cleaningservices.domain.model.Receipt;
import java.util.List;
import java.util.Optional;

// Define o que a aplicação precisa do banco para Recibos
public interface ReceiptRepositoryPort {
    Receipt save(Receipt receipt);
    List<Receipt> findAll();
    Optional<Receipt> findById(Long id);
    List<Receipt> findByEmitterId(Long emitterId);
}

