package com.example.cleaningservices.domain.model;

// Modelo de domínio do Cliente — sem anotações Spring ou JPA
// Cliente = quem contrata o serviço de limpeza (pode ser PF ou PJ)
// Endereço estruturado em campos separados para compatibilidade com NFS-e e ViaCEP
public class Client {

    private Long id;
    private String name;                  // nome completo ou razão social
    private String cpfCnpj;              // CPF ou CNPJ — único no banco
    private String email;                // opcional
    private String phone;                // opcional

    // endereço estruturado — campos separados para NFS-e e preenchimento automático via ViaCEP
    private String zipCode;              // CEP (ex: 04036-100) — usuário digita, ViaCEP preenche o resto
    private String street;              // logradouro (ex: R. Domingos de Morais) — vem do ViaCEP
    private String number;              // número (ex: 2584) — usuário digita
    private String complement;          // complemento (ex: Loja EC SM ST 0033) — opcional, usuário digita
    private String neighborhood;        // bairro (ex: Vila Mariana) — vem do ViaCEP
    private String city;                // cidade (ex: São Paulo) — vem do ViaCEP
    private String state;               // UF (ex: SP) — vem do ViaCEP

    private String municipalRegistration; // inscrição municipal — opcional, obrigatório para NFS-e

    public Client() {}

    public Client(Long id, String name, String cpfCnpj, String email, String phone,
                  String zipCode, String street, String number, String complement,
                  String neighborhood, String city, String state, String municipalRegistration) {
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
