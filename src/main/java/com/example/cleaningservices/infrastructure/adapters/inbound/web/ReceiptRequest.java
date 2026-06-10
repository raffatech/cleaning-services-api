package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class ReceiptRequest {

    @NotNull
    private Integer receiptNumber;

    @NotBlank
    private String clientName;

    @NotNull
    @Positive
    private BigDecimal value;

    @NotNull
    private Long emitterId;

    public Integer getReceiptNumber() { return receiptNumber; }
    public void setReceiptNumber(Integer receiptNumber) { this.receiptNumber = receiptNumber; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public Long getEmitterId() { return emitterId; }
    public void setEmitterId(Long emitterId) { this.emitterId = emitterId; }
}
