package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

// DTO de saída para orçamentos — usado nas listagens GET /quotes e GET /quotes/{id}
public class QuoteResponse {

    private Long id;
    private Integer quoteNumber;
    private Long emitterId;
    private String clientName;
    private String message;
    private List<QuoteItemResponse> items;
    private Double discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal subtotal;
    private BigDecimal finalValue;
    private String paymentConditions;
    private String paymentMethods;
    private LocalDate date;

    public QuoteResponse(Long id, Integer quoteNumber, Long emitterId, String clientName,
                         String message, List<QuoteItemResponse> items, Double discountPercent,
                         BigDecimal discountAmount, BigDecimal subtotal, BigDecimal finalValue,
                         String paymentConditions, String paymentMethods, LocalDate date) {
        this.id = id;
        this.quoteNumber = quoteNumber;
        this.emitterId = emitterId;
        this.clientName = clientName;
        this.message = message;
        this.items = items;
        this.discountPercent = discountPercent;
        this.discountAmount = discountAmount;
        this.subtotal = subtotal;
        this.finalValue = finalValue;
        this.paymentConditions = paymentConditions;
        this.paymentMethods = paymentMethods;
        this.date = date;
    }

    public Long getId() { return id; }
    public Integer getQuoteNumber() { return quoteNumber; }
    public Long getEmitterId() { return emitterId; }
    public String getClientName() { return clientName; }
    public String getMessage() { return message; }
    public List<QuoteItemResponse> getItems() { return items; }
    public Double getDiscountPercent() { return discountPercent; }
    public BigDecimal getDiscountAmount() { return discountAmount; }
    public BigDecimal getSubtotal() { return subtotal; }
    public BigDecimal getFinalValue() { return finalValue; }
    public String getPaymentConditions() { return paymentConditions; }
    public String getPaymentMethods() { return paymentMethods; }
    public LocalDate getDate() { return date; }
}

