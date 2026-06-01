package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.application.ports.outbound.EmployeeRepositoryPort;
import com.example.cleaningservices.domain.model.Employee;
import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.EmployeeEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EmployeeRepositoryAdapter implements EmployeeRepositoryPort {

    private final EmployeeJpaRepository employeeJpaRepository;

    public EmployeeRepositoryAdapter(EmployeeJpaRepository employeeJpaRepository) {
        this.employeeJpaRepository = employeeJpaRepository;
    }
    private EmployeeEntity convertToEntity(Employee employee) {
        return new EmployeeEntity(
                employee.getId(),
                employee.getName(),
                employee.getCpf(),
                employee.getRole(),
                employee.getEmail()
        );
    }
    private Employee convertToDomain(EmployeeEntity entity) {
        return new Employee(
                entity.getId(),
                entity.getName(),
                entity.getCpf(),
                entity.getRole(),
                entity.getEmail()
        );
    }
    @Override
    public Employee save(Employee employee) {
        EmployeeEntity entity = convertToEntity(employee);
        EmployeeEntity savedEntity = employeeJpaRepository.save(entity);
        return convertToDomain(savedEntity);
    }
    @Override
    public List<Employee> findAll() {
        return employeeJpaRepository.findAll()
                .stream()
                .map(this::convertToDomain)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<Employee> findById(Long id) {
        return employeeJpaRepository.findById(id).map(this::convertToDomain);
    }

    @Override
    public void deleteById(Long id) {
        employeeJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByCpf(String cpf) {
        return employeeJpaRepository.existsByCpf(cpf);
    }

    @Override
    public boolean existsByEmail(String email) {
        return employeeJpaRepository.existsByEmail(email);
    }
}

