package com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

// Entidade JPA — representa a tabela "service_invoices" no banco
// Guarda o registro de cada NFS-e emitida (ou simulada no modo mock)
@Entity
@Table(name = "service_invoices")
public class ServiceInvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long emitterId; // id do emitente (prestador de serviço)

    @Column(nullable = false)
    private Long clientId; // id do cliente (tomador de serviço)

    @Column(nullable = false)
    private BigDecimal value; // valor total da nota

    @Column(nullable = false)
    private String serviceDescription; // discriminação do serviço

    @Column
    private String serviceCode; // código do serviço (ex: "01406")

    @Column
    private String cityOfService; // município de prestação do serviço

    @Column(nullable = false)
    private LocalDate issueDate; // data de emissão

    @Column(nullable = false)
    private String status; // PENDENTE, EMITIDA, CANCELADA, ERRO

    @Column
    private String invoiceNumber; // número da nota emitida pela Prefeitura

    @Column
    private String verificationCode; // código de verificação

    @Column
    private String invoiceUrl; // link para consultar a nota online

    @Column
    private String focusNfeReference; // referência interna do Focus NFe

    public ServiceInvoiceEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmitterId() { return emitterId; }
    public void setEmitterId(Long emitterId) { this.emitterId = emitterId; }

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public String getServiceDescription() { return serviceDescription; }
    public void setServiceDescription(String serviceDescription) { this.serviceDescription = serviceDescription; }

    public String getServiceCode() { return serviceCode; }
    public void setServiceCode(String serviceCode) { this.serviceCode = serviceCode; }

    public String getCityOfService() { return cityOfService; }
    public void setCityOfService(String cityOfService) { this.cityOfService = cityOfService; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }

    public String getInvoiceUrl() { return invoiceUrl; }
    public void setInvoiceUrl(String invoiceUrl) { this.invoiceUrl = invoiceUrl; }

    public String getFocusNfeReference() { return focusNfeReference; }
    public void setFocusNfeReference(String focusNfeReference) { this.focusNfeReference = focusNfeReference; }
}

