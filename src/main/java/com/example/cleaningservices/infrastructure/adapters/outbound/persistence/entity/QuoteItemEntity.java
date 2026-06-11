package com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

// Entidade JPA — representa a tabela "quote_items" no banco
// Cada orçamento pode ter vários itens (relação 1 orçamento → N itens)
@Entity
@Table(name = "quote_items")
public class QuoteItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description; // ex: "Limpeza de vitrine"

    @Column(nullable = false)
    private BigDecimal value; // valor unitário

    @Column(nullable = false)
    private Double quantity; // quantidade (ex: 1.0)

    @Column
    private String unit; // unidade (ex: "un", "m²", "hr")

    // @ManyToOne significa: "muitos itens pertencem a um orçamento"
    // @JoinColumn define qual coluna no banco liga esse item ao orçamento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_id", nullable = false)
    private QuoteEntity quote; // referência ao orçamento pai

    public QuoteItemEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public Double getQuantity() { return quantity; }
    public void setQuantity(Double quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public QuoteEntity getQuote() { return quote; }
    public void setQuote(QuoteEntity quote) { this.quote = quote; }
}

