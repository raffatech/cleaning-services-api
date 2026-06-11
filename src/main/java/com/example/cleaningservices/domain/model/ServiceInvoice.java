package com.example.cleaningservices.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Modelo de domínio da Nota Fiscal Eletrônica de Serviços (NFS-e)
// Representa uma nota fiscal que será enviada à Prefeitura via Focus NFe
public class ServiceInvoice {

    private Long id;
    private Long emitterId;              // id do emitente (quem presta o serviço)
    private Long clientId;               // id do cliente (quem contrata)
    private BigDecimal value;            // valor total do serviço
    private String serviceDescription;  // discriminação do serviço (ex: "Limpeza de vitrine")
    private String serviceCode;          // código municipal (ex: "01406")
    private String cityOfService;        // município onde o serviço foi prestado (ex: "São Paulo")
    private LocalDate issueDate;         // data de emissão

    // campos preenchidos após retorno do Focus NFe / Prefeitura
    private String status;               // PENDENTE, EMITIDA, CANCELADA, ERRO
    private String invoiceNumber;        // número da NFS-e (ex: "1818")
    private String verificationCode;     // código de verificação (ex: "R8V7-EXHG")
    private String invoiceUrl;           // link para consultar a nota online
    private String focusNfeReference;    // referência interna única no Focus NFe

    public ServiceInvoice() {}

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

