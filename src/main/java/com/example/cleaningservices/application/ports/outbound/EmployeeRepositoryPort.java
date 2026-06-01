package com.example.cleaningservices.application.ports.outbound;

import com.example.cleaningservices.domain.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepositoryPort {
    Employee save(Employee employee);
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    void deleteById(Long id);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}

