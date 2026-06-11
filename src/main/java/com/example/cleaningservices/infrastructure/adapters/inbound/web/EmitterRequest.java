package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import jakarta.validation.constraints.NotBlank;

// DTO de entrada para criar ou atualizar um emitente
// Campos obrigatórios: companyName, signerName, cpfCnpj
// Todos os demais são opcionais — o emitente pode completar depois
public class EmitterRequest {

    @NotBlank(message = "Nome da empresa é obrigatório")
    private String companyName;

    @NotBlank(message = "Nome do responsável é obrigatório")
    private String signerName;

    @NotBlank(message = "CPF ou CNPJ é obrigatório")
    private String cpfCnpj;

    private String templatePath;          // opcional — template PDF do recibo (ex: template-cleaner.pdf)
    private String municipalRegistration; // opcional — necessário para emitir NFS-e
    private String phone;                 // opcional — exibido no cabeçalho do orçamento
    private String email;                 // opcional — exibido no cabeçalho do orçamento
    private String address;               // opcional — exibido no cabeçalho do orçamento
    private String logoPath;              // opcional — nome do arquivo da logo (ex: logo-talento.png)

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getSignerName() { return signerName; }
    public void setSignerName(String signerName) { this.signerName = signerName; }

    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

    public String getTemplatePath() { return templatePath; }
    public void setTemplatePath(String templatePath) { this.templatePath = templatePath; }

    public String getMunicipalRegistration() { return municipalRegistration; }
    public void setMunicipalRegistration(String municipalRegistration) { this.municipalRegistration = municipalRegistration; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getLogoPath() { return logoPath; }
    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }
}
