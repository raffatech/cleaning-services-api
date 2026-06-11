package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import jakarta.validation.constraints.NotBlank;

public class ClientRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "CPF/CNPJ is required")
    private String cpfCnpj;

    private String email;
    private String phone;

    // endereço estruturado — frontend usa ViaCEP para preencher street, neighborhood, city, state
    // usuário digita apenas: zipCode, number e complement
    private String zipCode;        // CEP (ex: 04036-100)
    private String street;         // logradouro — preenchido automaticamente pelo ViaCEP
    private String number;         // número — usuário digita
    private String complement;     // complemento — opcional, usuário digita
    private String neighborhood;   // bairro — preenchido automaticamente pelo ViaCEP
    private String city;           // cidade — preenchido automaticamente pelo ViaCEP
    private String state;          // UF — preenchido automaticamente pelo ViaCEP

    private String municipalRegistration; // inscrição municipal — opcional, para NFS-e

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
