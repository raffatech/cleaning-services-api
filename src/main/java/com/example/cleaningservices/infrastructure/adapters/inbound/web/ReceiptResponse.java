package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO de saída para recibos — usado nas listagens GET /receipts e GET /receipts/{id}
public class ReceiptResponse {

    private Long id;
    private Integer receiptNumber;
    private String clientName;
    private BigDecimal value;
    private Long emitterId;
    private LocalDate date;

    public ReceiptResponse(Long id, Integer receiptNumber, String clientName,
                           BigDecimal value, Long emitterId, LocalDate date) {
        this.id = id;
        this.receiptNumber = receiptNumber;
        this.clientName = clientName;
        this.value = value;
        this.emitterId = emitterId;
        this.date = date;
    }

    public Long getId() { return id; }
    public Integer getReceiptNumber() { return receiptNumber; }
    public String getClientName() { return clientName; }
    public BigDecimal getValue() { return value; }
    public Long getEmitterId() { return emitterId; }
    public LocalDate getDate() { return date; }
}

