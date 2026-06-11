package com.example.cleaningservices.domain.exception;

public class ReceiptNotFoundException extends RuntimeException {
    public ReceiptNotFoundException(Long id) {
        super("Receipt not found with id: " + id);
    }
}

