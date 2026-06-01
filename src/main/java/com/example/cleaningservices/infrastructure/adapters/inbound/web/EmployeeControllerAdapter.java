package com.example.cleaningservices.infrastructure.adapters.inbound.web;

import com.example.cleaningservices.application.ports.inbound.EmployeeServicePort;
import com.example.cleaningservices.domain.model.Employee;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/employees")
public class EmployeeControllerAdapter {

    private final EmployeeServicePort employeeServicePort;

    public EmployeeControllerAdapter(EmployeeServicePort employeeServicePort) {
        this.employeeServicePort = employeeServicePort;
    }
    private Employee convertToDomain(EmployeeRequest request) {
        return new Employee(
                null,
                request.getName(),
                request.getCpf(),
                request.getRole(),
                request.getEmail()
        );
    }
    private EmployeeResponse convertToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getCpf(),
                employee.getRole(),
                employee.getEmail()
        );
    }
    @PostMapping
    public ResponseEntity<EmployeeResponse> create(@Valid @RequestBody EmployeeRequest request) {
        Employee employee = convertToDomain(request);
        Employee created = employeeServicePort.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(created));
    }
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> findById(@PathVariable Long id) {
        Employee employee = employeeServicePort.findEmployeeById(id);
        return ResponseEntity.ok(convertToResponse(employee));
    }
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> findAll() {
        List<Employee> employees = employeeServicePort.findAllEmployees();
        List<EmployeeResponse> responses = employees.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        Employee employee = convertToDomain(request);
        Employee updated = employeeServicePort.updateEmployee(id, employee);
        return ResponseEntity.ok(convertToResponse(updated));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeServicePort.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}

