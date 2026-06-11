package com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Entidade JPA — representa a tabela "quotes" no banco
// Um orçamento tem uma lista de itens (QuoteItemEntity) — relação @OneToMany
@Entity
@Table(name = "quotes")
public class QuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quoteNumber; // número sequencial do orçamento

    @Column(nullable = false)
    private Long emitterId; // id do emitente (quem está orçando)

    @Column(nullable = false)
    private String clientName; // nome do cliente

    @Column
    private String message; // mensagem opcional

    @Column
    private Double discountPercent; // desconto em % (ex: 25.0)

    @Column
    private String paymentConditions; // ex: "Mensal", "À vista"

    @Column
    private String paymentMethods; // ex: "PIX, Dinheiro, Cartão de Débito"

    @Column(nullable = false)
    private LocalDate date; // data de geração do orçamento

    // @OneToMany significa: "um orçamento tem muitos itens"
    // cascade = ALL: quando salvar o orçamento, salva os itens automaticamente
    // orphanRemoval = true: quando remover o orçamento, remove os itens também
    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuoteItemEntity> items = new ArrayList<>();

    public QuoteEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getQuoteNumber() { return quoteNumber; }
    public void setQuoteNumber(Integer quoteNumber) { this.quoteNumber = quoteNumber; }

    public Long getEmitterId() { return emitterId; }
    public void setEmitterId(Long emitterId) { this.emitterId = emitterId; }

    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Double getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(Double discountPercent) { this.discountPercent = discountPercent; }

    public String getPaymentConditions() { return paymentConditions; }
    public void setPaymentConditions(String paymentConditions) { this.paymentConditions = paymentConditions; }

    public String getPaymentMethods() { return paymentMethods; }
    public void setPaymentMethods(String paymentMethods) { this.paymentMethods = paymentMethods; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public List<QuoteItemEntity> getItems() { return items; }
    public void setItems(List<QuoteItemEntity> items) { this.items = items; }
}

