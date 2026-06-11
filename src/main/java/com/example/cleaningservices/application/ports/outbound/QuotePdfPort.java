package com.example.cleaningservices.application.ports.outbound;

import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.Quote;

// Porta de saída — define o contrato para gerar PDFs de orçamento
// O domínio só conhece essa interface, nunca o PDFBox ou a implementação concreta
public interface QuotePdfPort {
    byte[] generate(Quote quote, Emitter emitter);
}

