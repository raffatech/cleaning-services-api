package com.example.cleaningservices.domain.exception;

public class ServiceInvoiceNotFoundException extends RuntimeException {
    public ServiceInvoiceNotFoundException(Long id) {
        super("Service invoice not found with id: " + id);
    }
}

