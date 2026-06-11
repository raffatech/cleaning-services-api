package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// DTO de um item do orçamento — representa uma linha da tabela
public class QuoteItemRequest {

    @NotBlank(message = "A descrição do item é obrigatória")
    private String description;    // descrição do serviço

    @NotNull
    @Positive(message = "O valor deve ser maior que zero")
    private BigDecimal value;      // valor unitário

    @NotNull
    @Positive(message = "A quantidade deve ser maior que zero")
    private Double quantity;       // quantidade (ex: 1.0, 2.5)

    private String unit = "un";    // unidade padrão: "un"

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
}

