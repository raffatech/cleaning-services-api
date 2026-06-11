package com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity;

import jakarta.persistence.*;

// @Entity diz ao JPA: "essa classe representa uma tabela no banco"
// @Table define o nome da tabela — aqui será "clients"
@Entity
@Table(name = "clients")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto incremento no banco
    private Long id;

    @Column(nullable = false)
    private String name;                // nome ou razão social

    @Column(nullable = false, unique = true)
    private String cpfCnpj;            // único no banco — não pode ter dois clientes com o mesmo CPF/CNPJ

    @Column
    private String email;              // nullable — nem todo cliente tem email cadastrado

    @Column
    private String phone;              // nullable

    // endereço estruturado — campos separados para NFS-e e ViaCEP
    @Column
    private String zipCode;          // CEP

    @Column
    private String street;          // logradouro

    @Column
    private String number;          // número

    @Column
    private String complement;      // complemento (nullable)

    @Column
    private String neighborhood;    // bairro

    @Column
    private String city;            // cidade

    @Column
    private String state;           // UF (ex: SP)

    @Column
    private String municipalRegistration; // inscrição municipal — nullable, necessário para NFS-e

    public ClientEntity() {}

    public ClientEntity(Long id, String name, String cpfCnpj, String email, String phone,
                        String zipCode, String street, String number, String complement,
                        String neighborhood, String city, String state,
                        String municipalRegistration) {
        this.id = id;
        this.name = name;
        this.cpfCnpj = cpfCnpj;
        this.email = email;
        this.phone = phone;
        this.zipCode = zipCode;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.municipalRegistration = municipalRegistration;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCpfCnpj() { return cpfCnpj; }
    public void setCpfCnpj(String cpfCnpj) { this.cpfCnpj = cpfCnpj; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }

    public String getStreet() { return street; }
    public void setStreet(String street) { this.street = street; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getComplement() { return complement; }
    public void setComplement(String complement) { this.complement = complement; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getMunicipalRegistration() { return municipalRegistration; }
    public void setMunicipalRegistration(String municipalRegistration) {
        this.municipalRegistration = municipalRegistration;
    }
}
