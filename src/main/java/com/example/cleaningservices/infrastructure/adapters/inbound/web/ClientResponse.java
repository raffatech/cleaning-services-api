package com.example.cleaningservices.infrastructure.adapters.inbound.web;

// DTO de saída — os dados que devolvemos na resposta da API
// Separado do ClientRequest para ter controle do que expor (nunca expor a @Entity diretamente)
public class ClientResponse {

    private Long id;
    private String name;
    private String cpfCnpj;
    private String email;
    private String phone;
    private String zipCode;
    private String street;
    private String number;
    private String complement;
    private String neighborhood;
    private String city;
    private String state;
    private String municipalRegistration;

    public ClientResponse() {}

    public ClientResponse(Long id, String name, String cpfCnpj, String email, String phone,
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
