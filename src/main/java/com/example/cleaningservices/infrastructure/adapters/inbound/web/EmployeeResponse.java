package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import com.example.cleaningservices.domain.model.EmployeeRole;

public class EmployeeResponse {

    private Long id;
    private String name;
    private String cpf;
    private EmployeeRole role;
    private String email;

    public EmployeeResponse() {
    }

    public EmployeeResponse(Long id, String name, String cpf, EmployeeRole role, String email) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.role = role;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }
}

