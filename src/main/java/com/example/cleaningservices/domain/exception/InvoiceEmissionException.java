package com.example.cleaningservices.domain.exception;

// Lançada quando a Prefeitura ou o Focus NFe rejeita a emissão da nota
public class InvoiceEmissionException extends RuntimeException {

    private final String detail; // detalhe técnico retornado pelo Focus NFe

    public InvoiceEmissionException(String message, String detail) {
        super(message);
        this.detail = detail;
    }

    public InvoiceEmissionException(String message) {
        super(message);
        this.detail = null;
    }

    public String getDetail() { return detail; }
}

