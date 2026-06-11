package com.example.cleaningservices.domain.model;

// Modelo de domínio do Emitente — sem anotações Spring ou JPA
// Representa quem emite o recibo (ex: Igor, Joel)
public class Emitter {

    private Long id;
    private String companyName;          // nome da empresa (ex: Cleaner Rodrigues)
    private String signerName;           // nome do responsável (ex: Igor Rodrigues dos Santos)
    private String cpfCnpj;              // CPF ou CNPJ
    private String templatePath;         // nome do template PDF (ex: template-cleaner-rodrigues.pdf)
    private String municipalRegistration; // inscrição municipal — obrigatório para NFS-e (ex: 7.968.888-8)
    private String phone;                // telefone exibido no cabeçalho do orçamento
    private String email;                // e-mail exibido no cabeçalho do orçamento
    private String address;              // endereço completo exibido no cabeçalho do orçamento
    private String logoPath;             // nome do arquivo da logo (ex: logo-talento.png) — opcional

    public Emitter() {}

    public Emitter(Long id, String companyName, String signerName, String cpfCnpj,
                   String templatePath, String municipalRegistration) {
        this.id = id;
        this.companyName = companyName;
        this.signerName = signerName;
        this.cpfCnpj = cpfCnpj;
        this.templatePath = templatePath;
        this.municipalRegistration = municipalRegistration;
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
