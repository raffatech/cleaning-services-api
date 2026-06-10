package com.example.cleaningservices.domain.service;

import com.example.cleaningservices.application.ports.inbound.ReceiptServicePort;
import com.example.cleaningservices.application.ports.outbound.EmitterRepositoryPort;
import com.example.cleaningservices.domain.exception.EmitterNotFoundException;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.Receipt;
import com.example.cleaningservices.infrastructure.adapters.outbound.pdf.PdfGeneratorAdapter;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class ReceiptService implements ReceiptServicePort {

    private final PdfGeneratorAdapter pdfGeneratorAdapter;
    private final EmitterRepositoryPort emitterRepositoryPort;

    public ReceiptService(PdfGeneratorAdapter pdfGeneratorAdapter,
                          EmitterRepositoryPort emitterRepositoryPort) {
        this.pdfGeneratorAdapter = pdfGeneratorAdapter;
        this.emitterRepositoryPort = emitterRepositoryPort;
    }

    @Override
    public byte[] generateReceipt(Receipt receipt) {
        // define a data de hoje automaticamente
        receipt.setDate(LocalDate.now());

        // busca o emitente no banco pelo id
        Emitter emitter = emitterRepositoryPort.findById(receipt.getEmitterId())
                .orElseThrow(() -> new EmitterNotFoundException(receipt.getEmitterId()));

        // manda para o adapter gerar o PDF com os dados do emitente
        return pdfGeneratorAdapter.generate(receipt, emitter);
    }
}