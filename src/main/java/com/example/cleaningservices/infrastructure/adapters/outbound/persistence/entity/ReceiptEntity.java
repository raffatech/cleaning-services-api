package com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// @Entity diz ao JPA: "essa classe representa uma tabela no banco"
// @Table define o nome da tabela — aqui será "receipts"
@Entity
@Table(name = "receipts")
public class ReceiptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // o banco gera o id automaticamente
    private Long id;

    @Column(nullable = false)
    private Integer receiptNumber; // número do recibo (ex: 5)

    @Column(nullable = false)
    private String clientName; // nome do cliente (ex: "Vivo VMT")

    @Column(nullable = false)
    private BigDecimal value; // valor em R$ (ex: 80.00)

    @Column(nullable = false)
    private Long emitterId; // referência ao emitente — guardamos só o id (não FK para simplificar)

    @Column(nullable = false)
    private LocalDate date; // data de emissão do recibo

    public ReceiptEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

