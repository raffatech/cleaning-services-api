package com.example.cleaningservices.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

// Modelo de domínio do Orçamento — sem anotações Spring ou JPA
public class Quote {

    private Long id;                       // id gerado pelo banco após salvar
    private Integer quoteNumber;           // número do orçamento
    private Long emitterId;                // id do emitente (quem está orçando)
    private String clientName;             // nome do cliente
    private String message;                // mensagem inicial (ex: "Aqui está o orçamento conforme solicitado")
    private List<QuoteItem> items;         // lista de itens/serviços
    private Double discountPercent;        // desconto em % (ex: 25.0) — opcional
    private String paymentConditions;      // ex: "Mensal", "À vista"
    private String paymentMethods;         // ex: "PIX, Dinheiro, Cartão de Débito"
    private LocalDate date;                // gerada automaticamente

    public Quote() {}

    // calcula o subtotal (soma de todos os itens)
    public BigDecimal getSubtotal() {
        if (items == null) return BigDecimal.ZERO;
        return items.stream()
                .map(QuoteItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // calcula o valor do desconto em R$
    public BigDecimal getDiscountAmount() {
        if (discountPercent == null || discountPercent == 0) return BigDecimal.ZERO;
        return getSubtotal()
                .multiply(BigDecimal.valueOf(discountPercent / 100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // calcula o valor final (subtotal - desconto)
    public BigDecimal getFinalValue() {
        return getSubtotal().subtract(getDiscountAmount());
    }

    public Integer getQuoteNumber() { return quoteNumber; }
    public void setQuoteNumber(Integer quoteNumber) { this.quoteNumber = quoteNumber; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmitterId() { return emitterId; }
    public void setEmitterId(Long emitterId) { this.emitterId = emitterId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<QuoteItem> getItems() { return items; }
    public void setItems(List<QuoteItem> items) { this.items = items; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    public String getPaymentConditions() { return paymentConditions; }
    public void setPaymentConditions(String paymentConditions) { this.paymentConditions = paymentConditions; }

    public String getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(String paymentMethods) { this.paymentMethods = paymentMethods; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}

