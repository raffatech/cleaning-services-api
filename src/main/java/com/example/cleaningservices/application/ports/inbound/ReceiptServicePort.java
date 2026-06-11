package com.example.cleaningservices.application.ports.inbound;

import com.example.cleaningservices.domain.model.Receipt;
import java.util.List;

public interface ReceiptServicePort {
    // gera o PDF e salva o recibo no banco
    byte[] generateReceipt(Receipt receipt);
    // busca o histórico de recibos
    List<Receipt> findAllReceipts();
    Receipt findReceiptById(Long id);
}