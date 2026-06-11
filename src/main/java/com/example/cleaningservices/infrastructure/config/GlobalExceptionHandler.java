package com.example.cleaningservices.infrastructure.config;

import com.example.cleaningservices.domain.exception.ClientNotFoundException;
import com.example.cleaningservices.domain.exception.EmitterNotFoundException;
import com.example.cleaningservices.domain.exception.EmployeeNotFoundException;
import com.example.cleaningservices.domain.exception.ReceiptNotFoundException;
import com.example.cleaningservices.domain.exception.QuoteNotFoundException;
import com.example.cleaningservices.domain.exception.ServiceInvoiceNotFoundException;
import com.example.cleaningservices.domain.exception.ValidationException;
import com.example.cleaningservices.domain.exception.InvoiceEmissionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeNotFound(EmployeeNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EmitterNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEmitterNotFound(EmitterNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleClientNotFound(ClientNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ReceiptNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleReceiptNotFound(ReceiptNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(QuoteNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleQuoteNotFound(QuoteNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ServiceInvoiceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleInvoiceNotFound(ServiceInvoiceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    // trata duplicidade de CPF, CNPJ, e-mail etc. em qualquer domínio
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidation(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getErrors());
    }

    // trata erros de emissão de NFS-e (Focus NFe / Prefeitura rejeitou a nota)
    @ExceptionHandler(InvoiceEmissionException.class)
    public ResponseEntity<Map<String, String>> handleInvoiceEmission(InvoiceEmissionException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        if (ex.getDetail() != null) {
            error.put("detail", ex.getDetail());
        }
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // tratador genérico — captura qualquer erro inesperado que não foi tratado acima
    // evita que o Spring exponha stack traces para o cliente
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleGeneric(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Erro interno no servidor");
        error.put("detail", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

