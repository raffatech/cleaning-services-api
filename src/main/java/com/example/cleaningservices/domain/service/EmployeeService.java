package com.example.cleaningservices.domain.service;

import com.example.cleaningservices.application.ports.inbound.EmployeeServicePort;
import com.example.cleaningservices.application.ports.outbound.EmployeeRepositoryPort;
import com.example.cleaningservices.domain.exception.EmployeeNotFoundException;
import com.example.cleaningservices.domain.exception.ValidationException;
import com.example.cleaningservices.domain.model.Employee;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService implements EmployeeServicePort {

    private final EmployeeRepositoryPort employeeRepositoryPort;

    public EmployeeService(EmployeeRepositoryPort employeeRepositoryPort) {
        this.employeeRepositoryPort = employeeRepositoryPort;
    }
    private void validateEmployee(Employee employee, Long id) {
        Map<String, String> errors = new HashMap<>();

        Employee existingEmployee = employeeRepositoryPort.findById(id != null ? id : -1L).orElse(null);
        if (employeeRepositoryPort.existsByCpf(employee.getCpf())) {
            if (existingEmployee == null || !existingEmployee.getCpf().equals(employee.getCpf())) {
                errors.put("cpf", "CPF: " + employee.getCpf() + " already exists.");
            }
        }
        if (employeeRepositoryPort.existsByEmail(employee.getEmail())){
            if(existingEmployee == null || !existingEmployee.getEmail().equals(employee.getEmail())){
                errors.put("email", "Email: " + employee.getEmail() + " already exists.");
            }
        }
        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        validateEmployee(employee, null);
        return employeeRepositoryPort.save(employee);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepositoryPort.findAll();
    }

    @Override
    public Employee findEmployeeById(Long id) {
        return employeeRepositoryPort.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        findEmployeeById(id);
        employee.setId(id);
        validateEmployee(employee, id);
        return employeeRepositoryPort.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        findEmployeeById(id);
        employeeRepositoryPort.deleteById(id);
    }
}

