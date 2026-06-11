package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

// DTO de entrada para gerar o orçamento
public class QuoteRequest {

    @NotNull(message = "O id do emitente é obrigatório")
    private Long emitterId;                // quem está fazendo o orçamento (Igor ou Joel)

    @NotNull(message = "O número do orçamento é obrigatório")
    private Integer quoteNumber;           // número sequencial (ex: 4)

    @NotBlank(message = "O nome do cliente é obrigatório")
    private String clientName;             // nome do cliente

    private String message;                // mensagem opcional (ex: "Aqui está o orçamento conforme solicitado")

    @NotEmpty(message = "O orçamento precisa ter ao menos um item")
    @Valid
    private List<QuoteItemRequest> items;  // lista de serviços/itens

    private Double discountPercent;        // desconto em % — opcional (ex: 25.0)

    private String paymentConditions;      // ex: "Mensal", "À vista", "30/60/90"

    private String paymentMethods;         // ex: "PIX, Dinheiro, Cartão de Débito, Boleto"

    public Long getEmitterId() { return emitterId; }
    public void setEmitterId(Long emitterId) { this.emitterId = emitterId; }

    public Integer getQuoteNumber() { return quoteNumber; }
    public void setQuoteNumber(Integer quoteNumber) { this.quoteNumber = quoteNumber; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public List<QuoteItemRequest> getItems() { return items; }
    public void setItems(List<QuoteItemRequest> items) { this.items = items; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    public String getPaymentConditions() { return paymentConditions; }
    public void setPaymentConditions(String paymentConditions) { this.paymentConditions = paymentConditions; }

    public String getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(String paymentMethods) { this.paymentMethods = paymentMethods; }
}

