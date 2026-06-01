package com.example.cleaningservices.application.ports.inbound;

import com.example.cleaningservices.domain.model.Employee;

import java.util.List;

public interface EmployeeServicePort {
    Employee createEmployee(Employee employee);
    List<Employee> findAllEmployees();
    Employee findEmployeeById(Long id);
    Employee updateEmployee(Long id, Employee employee);
    void deleteEmployee(Long id);
}

