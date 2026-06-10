package com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;

// Entidade JPA — representa a tabela "emitters" no banco de dados
@Entity
@Table(name = "emitters")
public class EmitterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String signerName;

    @Column(nullable = false, unique = true)
    private String cpfCnpj;

    @Column(nullable = false)
    private String templatePath;  // nome do template PDF do Canva

    public EmitterEntity() {}

    public EmitterEntity(Long id, String companyName, String signerName, String cpfCnpj, String templatePath) {
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
