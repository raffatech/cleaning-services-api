package com.example.cleaningservices.application.ports.outbound;

import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.Receipt;

// Porta de saída — define o contrato para gerar PDFs de recibo
// O domínio só conhece essa interface, nunca o PDFBox ou a implementação concreta
public interface ReceiptPdfPort {
    byte[] generate(Receipt receipt, Emitter emitter);
}

