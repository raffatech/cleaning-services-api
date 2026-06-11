package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

// Dados necessários para emitir uma NFS-e
// Simples! O usuário só seleciona emitente e cliente (já cadastrados) e digita valor + descrição
public class ServiceInvoiceRequest {

    @NotNull(message = "O id do emitente é obrigatório")
    private Long emitterId;              // quem presta o serviço (Igor ou Joel)

    @NotNull(message = "O id do cliente é obrigatório")
    private Long clientId;               // quem contratou (já cadastrado no sistema)

    @NotNull
    @Positive(message = "O valor deve ser maior que zero")
    private BigDecimal value;            // valor total do serviço

    @NotBlank(message = "A descrição do serviço é obrigatória")
    private String serviceDescription;  // ex: "Limpeza de vitrine"

    // código de serviço municipal — padrão para limpeza de imóveis em SP
    private String serviceCode = "01406";

    // município onde o serviço foi prestado — padrão São Paulo
    private String cityOfService = "São Paulo";

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
}

