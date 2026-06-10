package com.example.cleaningservices.domain.model;

// Modelo de domínio do Emitente — sem anotações Spring ou JPA
// Representa quem emite o recibo (ex: Igor, Joel)
public class Emitter {

    private Long id;
    private String companyName;   // nome da empresa (ex: Cleaner Rodrigues)
    private String signerName;    // nome do responsável (ex: Igor Rodrigues dos Santos)
    private String cpfCnpj;       // CPF ou CNPJ
    private String templatePath;  // nome do template PDF (ex: template-cleaner-rodrigues.pdf)

    public Emitter() {}

    public Emitter(Long id, String companyName, String signerName, String cpfCnpj, String templatePath) {
        this.id = id;
        this.companyName = companyName;
        this.signerName = signerName;
        this.cpfCnpj = cpfCnpj;
        this.templatePath = templatePath;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getSignerName() { return signerName; }
    public void setSignerName(String signerName) { this.signerName = signerName; }

    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

    public String getTemplatePath() { return templatePath; }
    public void setTemplatePath(String templatePath) { this.templatePath = templatePath; }
}
