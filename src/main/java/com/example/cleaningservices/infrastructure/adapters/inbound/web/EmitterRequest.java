package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import jakarta.validation.constraints.NotBlank;

public class EmitterRequest {

    @NotBlank
    private String companyName;

    @NotBlank
    private String signerName;

    @NotBlank
    private String cpfCnpj;

    @NotBlank
    private String templatePath;  // nome do template PDF (ex: template-cleaner-rodrigues.pdf)

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getSignerName() { return signerName; }
    public void setSignerName(String signerName) { this.signerName = signerName; }

    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

    public String getTemplatePath() { return templatePath; }
    public void setTemplatePath(String templatePath) { this.templatePath = templatePath; }
}
