package com.example.cleaningservices.domain.model;

import java.math.BigDecimal;

// Representa um item do orçamento (linha da tabela)
// Exemplo: "Limpeza de vitrine" | R$ 928,99 | 1.0 un | R$ 928,99
public class QuoteItem {

    private String description;   // descrição detalhada do serviço
    private BigDecimal value;     // valor unitário
    private Double quantity;      // quantidade (ex: 1.0)
    private String unit;          // unidade (ex: "un", "m²", "hr")

    public QuoteItem() {}

    public QuoteItem(String description, BigDecimal value, Double quantity, String unit) {
        this.description = description;
        this.value = value;
        this.quantity = quantity;
        this.unit = unit;
    }

    // calcula o total do item (valor × quantidade)
    public BigDecimal getTotal() {
        if (value == null || quantity == null) return BigDecimal.ZERO;
        return value.multiply(BigDecimal.valueOf(quantity));
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}

