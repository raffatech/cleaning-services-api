package com.example.cleaningservices.infrastructure.adapters.inbound.web;

public class EmitterResponse {

    private Long id;
    private String companyName;
    private String signerName;
    private String cpfCnpj;
    private String templatePath;

    public EmitterResponse(Long id, String companyName, String signerName, String cpfCnpj, String templatePath) {
        this.id = id;
        this.companyName = companyName;
        this.signerName = signerName;
        this.cpfCnpj = cpfCnpj;
        this.templatePath = templatePath;
    }

    public Long getId() { return id; }
    public String getCompanyName() { return companyName; }
    public String getSignerName() { return signerName; }
    public String getCpfCnpj() { return cpfCnpj; }
    public String getTemplatePath() { return templatePath; }
}
