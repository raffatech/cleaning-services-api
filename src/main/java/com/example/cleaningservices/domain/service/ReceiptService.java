package com.example.cleaningservices.domain.service;

import com.example.cleaningservices.application.ports.inbound.ReceiptServicePort;
import com.example.cleaningservices.application.ports.outbound.EmitterRepositoryPort;
import com.example.cleaningservices.application.ports.outbound.ReceiptPdfPort;
import com.example.cleaningservices.application.ports.outbound.ReceiptRepositoryPort;
import com.example.cleaningservices.domain.exception.EmitterNotFoundException;
import com.example.cleaningservices.domain.exception.ReceiptNotFoundException;
import com.example.cleaningservices.domain.model.Emitter;
import com.example.cleaningservices.domain.model.Receipt;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReceiptService implements ReceiptServicePort {

    // injeta a porta de PDF — o serviço não sabe que é PDFBox, só sabe que gera PDFs
    private final ReceiptPdfPort receiptPdfPort;
    private final EmitterRepositoryPort emitterRepositoryPort;
    private final ReceiptRepositoryPort receiptRepositoryPort;

    public ReceiptService(ReceiptPdfPort receiptPdfPort,
                          EmitterRepositoryPort emitterRepositoryPort,
                          ReceiptRepositoryPort receiptRepositoryPort) {
        this.receiptPdfPort = receiptPdfPort;
        this.emitterRepositoryPort = emitterRepositoryPort;
        this.receiptRepositoryPort = receiptRepositoryPort;
    }

    @Override
    public byte[] generateReceipt(Receipt receipt) {
        receipt.setDate(LocalDate.now());

        Emitter emitter = emitterRepositoryPort.findById(receipt.getEmitterId())
                .orElseThrow(() -> new EmitterNotFoundException(receipt.getEmitterId()));

        // gera o PDF primeiro — se falhar, não salva no banco
        byte[] pdf = receiptPdfPort.generate(receipt, emitter);

        // salva o recibo no banco somente após gerar o PDF com sucesso
        receiptRepositoryPort.save(receipt);

        return pdf;
    }

    @Override
    public List<Receipt> findAllReceipts() {
        return receiptRepositoryPort.findAll();
    }

    @Override
    public Receipt findReceiptById(Long id) {
        return receiptRepositoryPort.findById(id)
                .orElseThrow(() -> new ReceiptNotFoundException(id));
    }
}