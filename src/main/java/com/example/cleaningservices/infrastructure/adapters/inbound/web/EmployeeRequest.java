package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import com.example.cleaningservices.domain.model.EmployeeRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "CPF is required")
    private String cpf;

    @NotNull(message = "Role is required. Valid values: CLEANER, SUPERVISOR, ADMIN")
    private EmployeeRole role;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    public EmployeeRequest() {
    }

    public EmployeeRequest(String name, String cpf, EmployeeRole role, String email) {
        this.name = name;
        this.cpf = cpf;
        this.role = role;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public EmployeeRole getRole() {
        return role;
    }

    public void setRole(EmployeeRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

