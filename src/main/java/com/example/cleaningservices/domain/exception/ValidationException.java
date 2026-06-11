package com.example.cleaningservices.domain.exception;

import java.util.Map;

// Exceção genérica para validações de negócio (CPF/CNPJ duplicado, e-mail duplicado, etc.)
// Usada por todos os services: EmployeeService, ClientService, EmitterService
// Analogia: é como uma lista de erros de formulário — pode ter um ou mais erros juntos
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors; // campo → mensagem de erro

    public ValidationException(Map<String, String> errors) {
        super("Validation failed");
        this.errors = errors;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}

