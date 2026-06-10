package com.example.cleaningservices.application.ports.inbound;

import com.example.cleaningservices.domain.model.Receipt;

public interface ReceiptServicePort {

    // recebe o modelo do recibo e retorna o PDF em bytes
    byte[] generateReceipt(Receipt receipt);
}