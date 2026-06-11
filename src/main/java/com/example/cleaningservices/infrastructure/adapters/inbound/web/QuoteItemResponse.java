package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import java.math.BigDecimal;

// DTO de saída para um item do orçamento — aninhado dentro de QuoteResponse
public class QuoteItemResponse {

    private String description;
    private BigDecimal value;
    private Double quantity;
    private String unit;
    private BigDecimal total;

    public QuoteItemResponse(String description, BigDecimal value,
                             Double quantity, String unit, BigDecimal total) {
        this.description = description;
        this.value = value;
        this.quantity = quantity;
        this.unit = unit;
        this.total = total;
    }

    public String getDescription() { return description; }
    public BigDecimal getValue() { return value; }
    public Double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public BigDecimal getTotal() { return total; }
}

