package com.example.cleaningservices.infrastructure.adapters.outbound.persistence;

import com.example.cleaningservices.infrastructure.adapters.outbound.persistence.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}

