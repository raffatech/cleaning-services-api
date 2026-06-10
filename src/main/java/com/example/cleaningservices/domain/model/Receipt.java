package com.example.cleaningservices.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Receipt {

    private Integer receiptNumber;  // número do recibo
    private String clientName;      // nome do cliente (Recebemos de)
    private BigDecimal value;       // valor em R$
    private Long emitterId;         // id do emitente no banco
    private LocalDate date;         // gerada automaticamente

    public Receipt() {}

    public Receipt(Integer receiptNumber, String clientName, BigDecimal value,
                   Long emitterId, LocalDate date) {
        this.receiptNumber = receiptNumber;
        this.clientName = clientName;
        this.value = value;
        this.emitterId = emitterId;
        this.date = date;
    }

    public Integer getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(Integer receiptNumber) { this.receiptNumber = receiptNumber; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }


    public Long getEmitterId() { return emitterId; }
    public void setEmitterId(Long emitterId) { this.emitterId = emitterId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}