package com.example.cleaningservices.domain.model;

public class Employee {

    private Long id;
    private String name;
    private String cpf;
    private EmployeeRole role;
    private String email;

    public Employee() {
    }

    public Employee(Long id, String name, String cpf, EmployeeRole role, String email) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.role = role;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
