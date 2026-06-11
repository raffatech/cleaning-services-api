package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import java.math.BigDecimal;
import java.time.LocalDate;

// DTO de saída para NFS-e — usado tanto no POST quanto no GET
public class ServiceInvoiceResponse {

    private String status;
    private String invoiceNumber;
    private String verificationCode;
    private String invoiceUrl;
    private BigDecimal value;
    private String serviceDescription;
    private LocalDate issueDate;
    private String focusNfeReference;

    public ServiceInvoiceResponse() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getInvoiceNumber() { return invoiceNumber; }
    public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

    public String getVerificationCode() { return verificationCode; }
    public void setVerificationCode(String verificationCode) { this.verificationCode = verificationCode; }

    public String getInvoiceUrl() { return invoiceUrl; }
    public void setInvoiceUrl(String invoiceUrl) { this.invoiceUrl = invoiceUrl; }

    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }

    public String getServiceDescription() { return serviceDescription; }
    public void setServiceDescription(String serviceDescription) { this.serviceDescription = serviceDescription; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public String getFocusNfeReference() { return focusNfeReference; }
    public void setFocusNfeReference(String focusNfeReference) { this.focusNfeReference = focusNfeReference; }
}
